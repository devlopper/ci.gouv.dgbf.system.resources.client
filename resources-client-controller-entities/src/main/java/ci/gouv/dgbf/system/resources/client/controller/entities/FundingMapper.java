package ci.gouv.dgbf.system.resources.client.controller.entities;
import ci.gouv.dgbf.system.resources.server.representation.entities.FundingDto;
import org.cyk.utility.__kernel__.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class FundingMapper extends AbstractMapperSourceDestinationImpl<Funding, FundingDto> {
	private static final long serialVersionUID = 1L;
    	
}