package ci.gouv.dgbf.system.resources.client.controller.entities;
import ci.gouv.dgbf.system.resources.server.representation.entities.FundingSourceDto;
import org.cyk.utility.__kernel__.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class FundingSourceMapper extends AbstractMapperSourceDestinationImpl<FundingSource, FundingSourceDto> {
	private static final long serialVersionUID = 1L;
    	
}