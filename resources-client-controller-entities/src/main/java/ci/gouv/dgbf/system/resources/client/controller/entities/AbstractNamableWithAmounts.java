package ci.gouv.dgbf.system.resources.client.controller.entities;

import javax.persistence.MappedSuperclass;

import org.cyk.utility.__kernel__.object.__static__.controller.AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl;
import org.cyk.utility.__kernel__.string.StringHelper;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@MappedSuperclass @Getter @Setter @Accessors(chain=true)
public abstract class AbstractNamableWithAmounts extends AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl {

	protected Amounts amounts;
	
	public Amounts getAmounts(Boolean injectIfNull) {
		if(amounts == null && Boolean.TRUE.equals(injectIfNull))
			amounts = new Amounts();
		return amounts;
	}
	
	@Override
	public String toString() {
		if(StringHelper.isBlank(identifier))
			if(StringHelper.isBlank(code))
				return name;
			else
				if(StringHelper.isBlank(name))
					return code;
				else
					return code+" "+name;
		else
			if(StringHelper.isBlank(code))
				if(StringHelper.isBlank(name))
					return identifier;
				else
					return name;
			else
				if(StringHelper.isBlank(name))
					return code;
				else
					return code+" "+name;
	}
	
	public static final String FIELD_AMOUNTS = "amounts";
}