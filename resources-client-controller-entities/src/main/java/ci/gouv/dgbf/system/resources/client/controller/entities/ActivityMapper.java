package ci.gouv.dgbf.system.resources.client.controller.entities;
import ci.gouv.dgbf.system.resources.server.representation.entities.ActivityDto;
import org.cyk.utility.__kernel__.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class ActivityMapper extends AbstractMapperSourceDestinationImpl<Activity, ActivityDto> {
	private static final long serialVersionUID = 1L;
    	
}