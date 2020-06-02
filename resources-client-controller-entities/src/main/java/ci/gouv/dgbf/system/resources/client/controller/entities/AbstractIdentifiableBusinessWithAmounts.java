package ci.gouv.dgbf.system.resources.client.controller.entities;

import javax.persistence.MappedSuperclass;

import org.cyk.utility.__kernel__.object.__static__.controller.AbstractDataIdentifiableSystemStringIdentifiableBusinessStringImpl;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@MappedSuperclass @Getter @Setter @Accessors(chain=true)
public abstract class AbstractIdentifiableBusinessWithAmounts extends AbstractDataIdentifiableSystemStringIdentifiableBusinessStringImpl {

	protected Amounts amounts;
	
	public Amounts getAmounts(Boolean injectIfNull) {
		if(amounts == null && Boolean.TRUE.equals(injectIfNull))
			amounts = new Amounts();
		return amounts;
	}
	
	@Override
	public String toString() {
		return code;
	}
	
	public static final String FIELD_AMOUNTS = "amounts";
}