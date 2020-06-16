package ci.gouv.dgbf.system.resources.client.controller.entities;

import java.io.Serializable;
import java.util.Collection;

import ci.gouv.dgbf.system.resources.server.business.api.ActivityBusiness;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class Activity extends AbstractNamableWithAmounts implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private BudgetaryActVersion budgetaryActVersion;
	private BudgetSpecializationUnit budgetSpecializationUnit;
	private Collection<Resource> resources;

	public Boolean getComputable() {
		return ActivityBusiness.isComputable(code);
	}
}