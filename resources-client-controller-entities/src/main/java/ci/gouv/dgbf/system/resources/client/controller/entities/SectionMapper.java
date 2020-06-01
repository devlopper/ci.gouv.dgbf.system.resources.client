package ci.gouv.dgbf.system.resources.client.controller.entities;
import ci.gouv.dgbf.system.resources.server.representation.entities.SectionDto;
import org.cyk.utility.__kernel__.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class SectionMapper extends AbstractMapperSourceDestinationImpl<Section, SectionDto> {
	private static final long serialVersionUID = 1L;
    	
}