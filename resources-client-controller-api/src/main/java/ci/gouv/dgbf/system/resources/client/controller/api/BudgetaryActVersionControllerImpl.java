package ci.gouv.dgbf.system.resources.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.resources.client.controller.entities.BudgetaryActVersion;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

@ApplicationScoped
public class BudgetaryActVersionControllerImpl extends AbstractControllerEntityImpl<BudgetaryActVersion> implements BudgetaryActVersionController,Serializable {
	private static final long serialVersionUID = 1L;
	
}
