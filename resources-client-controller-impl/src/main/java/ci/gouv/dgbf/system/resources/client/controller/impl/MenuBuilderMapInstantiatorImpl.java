package ci.gouv.dgbf.system.resources.client.controller.impl;

import java.io.Serializable;
import java.security.Principal;

import org.cyk.utility.__kernel__.icon.Icon;
import org.cyk.utility.client.controller.component.menu.MenuBuilder;
import org.cyk.utility.client.controller.component.menu.MenuItemBuilder;

@ci.gouv.dgbf.system.resources.server.annotation.System
public class MenuBuilderMapInstantiatorImpl extends org.cyk.utility.client.controller.component.menu.AbstractMenuBuilderMapInstantiatorImpl implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	protected void __instantiateSessionMenuBuilderItems__(Object key, MenuBuilder sessionMenuBuilder, Object request,Principal principal) {
		sessionMenuBuilder.addItems(
				__inject__(MenuItemBuilder.class).setCommandableName("Resources").setCommandableIcon(Icon.SUITCASE)
				.addChild(
						__inject__(MenuItemBuilder.class).setCommandableName("Saisir les montants").setCommandableNavigationIdentifier("resourceEditInitialsView").setCommandableIcon(Icon.PENCIL)
						,__inject__(MenuItemBuilder.class).setCommandableName("Sélectionner une version").setCommandableNavigationIdentifier("budgetaryActVersionSelectView").setCommandableIcon(Icon.FILE)
						,__inject__(MenuItemBuilder.class).setCommandableName("Saisir les comptes bailleurs").setCommandableNavigationIdentifier("fundingSourceLessorEditEconomicNaturesView").setCommandableIcon(Icon.BANK)
						)
				);		
	}	
	
}
