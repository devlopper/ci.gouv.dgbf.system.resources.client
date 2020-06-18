package ci.gouv.dgbf.system.resources.client.controller.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.core.Response;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntitySaver;

import ci.gouv.dgbf.system.resources.server.business.api.ActivityBusiness;
import ci.gouv.dgbf.system.resources.server.business.api.FundingSourceLessorBusiness;
import ci.gouv.dgbf.system.resources.server.business.api.ResourceBusiness;
import ci.gouv.dgbf.system.resources.server.representation.api.ActivityRepresentation;
import ci.gouv.dgbf.system.resources.server.representation.api.FundingSourceLessorRepresentation;
import ci.gouv.dgbf.system.resources.server.representation.api.ResourceRepresentation;
import ci.gouv.dgbf.system.resources.server.representation.entities.ActivityDto;
import ci.gouv.dgbf.system.resources.server.representation.entities.FundingSourceLessorDto;
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
			else if(FundingSourceLessorBusiness.SAVE_ECONOMIC_NATURES.equals(arguments.getRepresentationArguments().getActionIdentifier()) && arguments.getRepresentation() == null)
				arguments.setRepresentation(FundingSourceLessorRepresentation.getProxy());
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
		else if(arguments != null && FundingSourceLessorBusiness.SAVE_ECONOMIC_NATURES.equals(arguments.getActionIdentifier())) {
			Collection<FundingSourceLessorDto> fundingSourceLessorDtos = new ArrayList<>();
			if(CollectionHelper.isNotEmpty(creatables))
				for(Object index : creatables)
					fundingSourceLessorDtos.add((FundingSourceLessorDto) index);
			if(CollectionHelper.isNotEmpty(updatables))
				for(Object index : updatables)
					fundingSourceLessorDtos.add((FundingSourceLessorDto) index);
			if(CollectionHelper.isNotEmpty(deletables))
				for(Object index : deletables)
					fundingSourceLessorDtos.add(((FundingSourceLessorDto) index).setDeletable(Boolean.TRUE));
			return ((FundingSourceLessorRepresentation)representation).saveEconomicNatures(fundingSourceLessorDtos);
		}
		return super.save(representation, creatables, updatables, deletables, arguments);
	}
}