package ci.gouv.dgbf.system.resources.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.resources.client.controller.entities.FundingSourceLessor;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

@ApplicationScoped
public class FundingSourceLessorControllerImpl extends AbstractControllerEntityImpl<FundingSourceLessor> implements FundingSourceLessorController,Serializable {
	private static final long serialVersionUID = 1L;
	
}
