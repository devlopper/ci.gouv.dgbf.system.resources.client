package ci.gouv.dgbf.system.resources.client.controller.impl;

import javax.servlet.ServletContextEvent;

import org.cyk.user.interface_.theme.web.jsf.primefaces.atlantis.dgbf.DesktopDefault;
import org.cyk.utility.__kernel__.DependencyInjection;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractServletContextListener;

public interface ServletContextListener extends javax.servlet.ServletContextListener {

	@Override
	default void contextInitialized(ServletContextEvent sce) {
		javax.servlet.ServletContextListener.super.contextInitialized(sce);
		initialize(sce);
	}
	
	default void initialize(ServletContextEvent sce) {
		AbstractServletContextListener.initializeFromStatic(sce.getServletContext(), null);
		DesktopDefault.initialize();		
		DependencyInjection.inject(ApplicationScopeLifeCycleListener.class).initialize(null);
	}
	
}
