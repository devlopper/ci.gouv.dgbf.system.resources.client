package ci.gouv.dgbf.system.resources.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.resources.client.controller.entities.Budget;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

@ApplicationScoped
public class BudgetControllerImpl extends AbstractControllerEntityImpl<Budget> implements BudgetController,Serializable {
	private static final long serialVersionUID = 1L;
	
}
