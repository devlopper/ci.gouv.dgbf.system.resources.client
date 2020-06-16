package ci.gouv.dgbf.system.resources.client.controller.entities;

import java.io.Serializable;

import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.__kernel__.object.AbstractObject;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AbstractAmounts extends AbstractObject implements Serializable {

	private Long initial;
	
	public AbstractAmounts incrementInitial(Long value) {
		if(value == null)
			return this;
		initial = (Long) NumberHelper.add(initial,value);		
		return this;
	}
	
	public AbstractAmounts decrementInitial(Long value) {
		if(value == null)
			return this;
		initial = (Long) NumberHelper.subtract(initial,value);		
		return this;
	}
	
	@Override
	public String toString() {
		return String.format(TO_STRING_FORMAT, initial);
	}
	
	public static final String TO_STRING_FORMAT = "I=%s";
	
	public static final String FIELD_INITIAL = "initial";
}