package com.soluvas.cxf;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

/**
 * JAX-RS Server Manager that controls CXF {@link JAXRSServerFactoryBean} with
 * dynamic JAX-RS resources provided by {@link java.lang.Iterable} OSGi services.
 * @author rudi
 */
public class DynamicJaxrsManager implements ServiceTrackerCustomizer, JaxrsManager {
	
	private transient Logger log = LoggerFactory
			.getLogger(DynamicJaxrsManager.class);

	private BundleContext bundleContext;
	private Server server;
	private String address;
	private List<Object> providers;
	private List<List<Object>> serviceBeanLists = new CopyOnWriteArrayList<List<Object>>();
	
	public DynamicJaxrsManager() {
		super();
	}
	
	public DynamicJaxrsManager(BundleContext bundleContext, String address, List<Object> providers) {
		super();
		this.bundleContext = bundleContext;
		this.address = address;
		this.providers = providers;
	}

	/* (non-Javadoc)
	 * @see com.soluvas.cxf.JaxrsManager#init()
	 */
	@Override
	public void init() {
		createServer();
	}
	
	/* (non-Javadoc)
	 * @see com.soluvas.cxf.JaxrsManager#getServiceBeans()
	 */
	@Override
	public List<Object> getServiceBeans() {
		List<Object> serviceBeans = ImmutableList.copyOf(Iterables.concat(serviceBeanLists));
		return serviceBeans;
	}
	
	private void createServer() {
		destroyServer();
		
		List<Object> serviceBeans = getServiceBeans();
		if (serviceBeans.isEmpty()) {
			log.warn("No JAX-RS resources found, not starting server");
		} else {
			log.info("Starting JAX-RS server with {} resources: {}", serviceBeans.size(), serviceBeans);
			
			JAXRSServerFactoryBean serverFactoryBean = new JAXRSServerFactoryBean();
			serverFactoryBean.setAddress(address);
			serverFactoryBean.setProviders(providers);
			serverFactoryBean.setServiceBeans(serviceBeans);
			server = serverFactoryBean.create();
		}
	}

	/* (non-Javadoc)
	 * @see com.soluvas.cxf.JaxrsManager#destroy()
	 */
	@Override
	public void destroy() {
		destroyServer();
		serviceBeanLists.clear();
	}

	/**
	 * 
	 */
	protected void destroyServer() {
		if (server != null) {
			server.destroy();
			server = null;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.soluvas.cxf.JaxrsManager#addingService(org.osgi.framework.ServiceReference)
	 */
	@Override
	public Object addingService(ServiceReference reference) {
		Iterable resourceContainer = (Iterable)bundleContext.getService(reference);
		try {
			List serviceBeans = ImmutableList.copyOf( resourceContainer );
			log.info("Adding {} new JAX-RS resources: {}", serviceBeans.size(), serviceBeans);
			serviceBeanLists.add(serviceBeans);
			createServer();
			return serviceBeans;
		} finally {
			bundleContext.ungetService(reference);
		}
	}

	/* (non-Javadoc)
	 * @see com.soluvas.cxf.JaxrsManager#modifiedService(org.osgi.framework.ServiceReference, java.lang.Object)
	 */
	@Override
	public void modifiedService(ServiceReference reference, Object service) {
	}

	/* (non-Javadoc)
	 * @see com.soluvas.cxf.JaxrsManager#removedService(org.osgi.framework.ServiceReference, java.lang.Object)
	 */
	@Override
	public void removedService(ServiceReference reference, Object service) {
		List serviceBeans = (List)service;
		log.info("Removing {} JAX-RS resources: {}", serviceBeans.size(), serviceBeans);
		serviceBeanLists.remove(service);
		createServer();
	}

	/* (non-Javadoc)
	 * @see com.soluvas.cxf.JaxrsManager#getAddress()
	 */
	@Override
	public String getAddress() {
		return address;
	}
	
}
