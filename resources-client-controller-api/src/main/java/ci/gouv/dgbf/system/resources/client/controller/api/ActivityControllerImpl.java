package ci.gouv.dgbf.system.resources.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.resources.client.controller.entities.Activity;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

@ApplicationScoped
public class ActivityControllerImpl extends AbstractControllerEntityImpl<Activity> implements ActivityController,Serializable {
	private static final long serialVersionUID = 1L;
	
}
