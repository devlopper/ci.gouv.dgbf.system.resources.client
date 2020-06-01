package ci.gouv.dgbf.system.resources.client.controller.entities;
import ci.gouv.dgbf.system.resources.server.representation.entities.ResourceDto;
import org.cyk.utility.__kernel__.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class ResourceMapper extends AbstractMapperSourceDestinationImpl<Resource, ResourceDto> {
	private static final long serialVersionUID = 1L;
    	
}