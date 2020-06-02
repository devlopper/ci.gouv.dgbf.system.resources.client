package ci.gouv.dgbf.system.resources.client.controller.entities;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class BudgetSpecializationUnit extends AbstractNamableWithAmounts implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Section section;
	private BudgetSpecializationUnitCategory category;

}