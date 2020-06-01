package ci.gouv.dgbf.system.resources.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.resources.client.controller.entities.Resource;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

@ApplicationScoped
public class ResourceControllerImpl extends AbstractControllerEntityImpl<Resource> implements ResourceController,Serializable {
	private static final long serialVersionUID = 1L;
	
}
