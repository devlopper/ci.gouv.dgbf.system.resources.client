package ci.gouv.dgbf.system.resources.client.controller.impl;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.servlet.ServletContext;

import org.cyk.user.interface_.theme.web.jsf.primefaces.atlantis.dgbf.DesktopDefault;
import org.cyk.utility.__kernel__.AbstractApplicationScopeLifeCycleListener;
import org.cyk.utility.__kernel__.DependencyInjection;
import org.cyk.utility.__kernel__.controller.EntitySaver;
import org.cyk.utility.client.controller.component.menu.MenuBuilderMapInstantiator;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractServletContextListener;

@ApplicationScoped
public class ApplicationScopeLifeCycleListener extends AbstractApplicationScopeLifeCycleListener implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public void __initialize__(Object object) {
		__inject__(ci.gouv.dgbf.system.resources.client.controller.api.ApplicationScopeLifeCycleListener.class).initialize(null);
		__setQualifierClassTo__(ci.gouv.dgbf.system.resources.server.annotation.System.class, MenuBuilderMapInstantiator.class,EntitySaver.class);
	}
	
	@Override
	public void __destroy__(Object object) {}
	
	/**/
	
	public static void initializeContext(ServletContext context) {
		AbstractServletContextListener.initializeFromStatic(context, null);
		DesktopDefault.initialize();
		DependencyInjection.inject(ApplicationScopeLifeCycleListener.class).initialize(null);
	}
}
