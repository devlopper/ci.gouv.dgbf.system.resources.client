package ci.gouv.dgbf.system.resources.client.controller.entities;
import ci.gouv.dgbf.system.resources.server.representation.entities.ExpenditureDto;
import org.cyk.utility.__kernel__.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class ExpenditureMapper extends AbstractMapperSourceDestinationImpl<Expenditure, ExpenditureDto> {
	private static final long serialVersionUID = 1L;
    	
}