package ci.gouv.dgbf.system.resources.client.controller.entities;

import java.io.Serializable;

import org.cyk.utility.client.controller.data.AbstractDataIdentifiableSystemStringImpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor //@Accessors(chain=true)
public class FundingSourceLessor extends AbstractDataIdentifiableSystemStringImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private FundingSource fundingSource;
	//private Lessor lessor;
	private String lessorIdentifier;
	private EconomicNature economicNature;

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return fundingSource+" | "+lessorIdentifier+" | "+economicNature;
	}
}