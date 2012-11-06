package com.soluvas.cxf.shell;

import java.util.List;

import javax.ws.rs.Path;

import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.basic.SimpleCommand;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.soluvas.cxf.DynamicJaxrsManager;
import com.soluvas.cxf.JaxrsManager;

/**
 * For how to publish GoGo commands in OSGi, see {@link SimpleCommand}.
 * @author ceefour
 */
@Command(scope="jaxrs", name="ls", description="List JAX-RS resources.")
public class JaxrsLsCommand extends OsgiCommandSupport {
	
	private transient Logger log = LoggerFactory.getLogger(JaxrsLsCommand.class);
	
	private List<JaxrsManager> managers;
	
	public JaxrsLsCommand(List<JaxrsManager> managers) {
		super();
		this.managers = managers;
	}

	/* (non-Javadoc)
	 * @see org.apache.karaf.shell.console.AbstractAction#doExecute()
	 */
	@Override
	protected Object doExecute() throws Exception {
		System.out.format("%3s | %-20s | %-30s | %s\n",
				"#", "Server", "Path", "Class");
		int i = 0;
		for (JaxrsManager manager : managers) {
			for (Object resource : manager.getServiceBeans()) {
				Path pathAnnotation = resource.getClass().getAnnotation(Path.class);
				String path = pathAnnotation.value();
				System.out.format("%3d | %-20s | %-30s | %s\n",
					++i, manager.getAddress(), path, resource.getClass().getName() );
			}
		}
		System.out.format("%d JAX-RS resources\n", i);
		return null;
	}

}
