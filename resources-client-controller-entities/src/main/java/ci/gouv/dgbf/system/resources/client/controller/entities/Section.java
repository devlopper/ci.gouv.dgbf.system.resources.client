package ci.gouv.dgbf.system.resources.client.controller.entities;

import java.io.Serializable;
import java.util.Collection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class Section extends AbstractNamableWithAmounts implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Collection<BudgetSpecializationUnitCategory> budgetSpecializationUnitCategories;
	private Collection<BudgetSpecializationUnit> budgetSpecializationUnits;
	
}