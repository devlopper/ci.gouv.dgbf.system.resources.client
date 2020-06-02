package ci.gouv.dgbf.system.resources.client.controller.entities;

import java.io.Serializable;

import org.cyk.utility.__kernel__.object.AbstractObject;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AbstractAmounts extends AbstractObject implements Serializable {

	private Long initial;
	
	@Override
	public String toString() {
		return String.format(TO_STRING_FORMAT, initial);
	}
	
	public static final String TO_STRING_FORMAT = "I=%s";
	
	public static final String FIELD_INITIAL = "initial";
}