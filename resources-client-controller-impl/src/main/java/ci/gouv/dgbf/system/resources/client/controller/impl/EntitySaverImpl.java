package ci.gouv.dgbf.system.resources.client.controller.impl;

import java.io.Serializable;
import java.util.Collection;

import javax.ws.rs.core.Response;

import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntitySaver;

import ci.gouv.dgbf.system.resources.server.business.api.ActivityBusiness;
import ci.gouv.dgbf.system.resources.server.business.api.ResourceBusiness;
import ci.gouv.dgbf.system.resources.server.representation.api.ActivityRepresentation;
import ci.gouv.dgbf.system.resources.server.representation.api.ResourceRepresentation;
import ci.gouv.dgbf.system.resources.server.representation.entities.ActivityDto;
import ci.gouv.dgbf.system.resources.server.representation.entities.ResourceDto;

@ci.gouv.dgbf.system.resources.server.annotation.System
public class EntitySaverImpl extends EntitySaver.AbstractImpl implements Serializable {

	@Override
	protected <T> void prepare(Class<T> controllerEntityClass,Arguments<T> arguments) {
		if(arguments.getRepresentationArguments() != null) {
			if(ResourceBusiness.SAVE_INITIALS.equals(arguments.getRepresentationArguments().getActionIdentifier()) && arguments.getRepresentation() == null)
				arguments.setRepresentation(ResourceRepresentation.getProxy());
			else if(ActivityBusiness.SAVE_INITIALS_FROM_COMPUTATION.equals(arguments.getRepresentationArguments().getActionIdentifier()) && arguments.getRepresentation() == null)
				arguments.setRepresentation(ActivityRepresentation.getProxy());
		}
		super.prepare(controllerEntityClass, arguments);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected <T> Response save(Object representation, Collection<?> creatables, Collection<?> updatables,Collection<?> deletables, org.cyk.utility.__kernel__.representation.Arguments arguments) {
		if(arguments != null && ResourceBusiness.SAVE_INITIALS.equals(arguments.getActionIdentifier()))
			return ((ResourceRepresentation)representation).saveInitials((Collection<ResourceDto>) updatables);
		else if(arguments != null && ActivityBusiness.SAVE_INITIALS_FROM_COMPUTATION.equals(arguments.getActionIdentifier()))
			return ((ActivityRepresentation)representation).saveInitialsFromComputation((Collection<ActivityDto>) updatables);
		return super.save(representation, creatables, updatables, deletables, arguments);
	}
}