package com.soluvas.cxf;

import java.util.List;

import org.osgi.framework.ServiceReference;

public interface JaxrsManager {

	public abstract void init();

	public abstract List<Object> getServiceBeans();

	public abstract void destroy();

	public abstract Object addingService(ServiceReference reference);

	public abstract void modifiedService(ServiceReference reference,
			Object service);

	public abstract void removedService(ServiceReference reference,
			Object service);

	/**
	 * @return the address
	 */
	public abstract String getAddress();

}