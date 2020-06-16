package ci.gouv.dgbf.system.resources.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.resources.client.controller.entities.Funding;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

@ApplicationScoped
public class FundingControllerImpl extends AbstractControllerEntityImpl<Funding> implements FundingController,Serializable {
	private static final long serialVersionUID = 1L;
	
}
