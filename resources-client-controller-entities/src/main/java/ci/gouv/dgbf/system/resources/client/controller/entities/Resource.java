package ci.gouv.dgbf.system.resources.client.controller.entities;

import java.io.Serializable;

import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Choices;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Choices.Count;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Input;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoice;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceOne;
import org.cyk.utility.client.controller.data.AbstractDataIdentifiableSystemStringImpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class Resource extends AbstractDataIdentifiableSystemStringImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private BudgetSpecializationUnit budgetSpecializationUnit;
	private BudgetaryActVersion budgetaryActVersion;
	private Budget budget;
	private Activity activity;
	@Input @InputChoice(choices = @Choices(count = Count.ALL)) @InputChoiceOne
	private EconomicNature economicNature;
	
	private Amounts amounts;
	private String sectionAsString,activityAsString,budgetSpecializationUnitAsString,economicNatureAsString;
	
	public Amounts getAmounts(Boolean injectIfNull) {
		if(amounts == null && Boolean.TRUE.equals(injectIfNull))
			amounts = new Amounts();
		return amounts;
	}
	
	/**/
	
	public static final String FIELD_BUDGET = "budget";
	public static final String FIELD_ACTIVITY = "activity";
	public static final String FIELD_ECONOMIC_NATURE = "economicNature";
	public static final String FIELD_AMOUNTS = "amounts";
	public static final String FIELD_SECTION_AS_STRING = "sectionAsString";
	public static final String FIELD_ACTIVITY_AS_STRING = "activityAsString";
	public static final String FIELD_BUDGET_SPECIALIZATION_UNIT_AS_STRING = "budgetSpecializationUnitAsString";
	public static final String FIELD_ECONOMIC_NATURE_AS_STRING = "economicNatureAsString";
	
}