package ci.gouv.dgbf.system.resources.client.controller.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.object.__static__.controller.AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor //@Accessors(chain=true)
public class Lessor extends AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl implements Serializable {
	private static final long serialVersionUID = 1L;

	private Collection<EconomicNature> economicNatures;
	private Collection<FundingSourceLessor> fundingSourceLessors;
	private EconomicNature tresorEconomicNature;
	private EconomicNature donEconomicNature;
	private EconomicNature empruntEconomicNature;

	public Collection<EconomicNature> getEconomicNatures(Boolean injectIfNull) {
		if(economicNatures == null && Boolean.TRUE.equals(injectIfNull))
			economicNatures = new ArrayList<>();
		return economicNatures;
	}
	
	public Collection<FundingSourceLessor> getFundingSourceLessors(Boolean injectIfNull) {
		if(fundingSourceLessors == null && Boolean.TRUE.equals(injectIfNull))
			fundingSourceLessors = new ArrayList<>();
		return fundingSourceLessors;
	}
	
	public List<EconomicNature> getEconomicNatures(List<FundingSource> fundingSources) {
		if(CollectionHelper.isEmpty(fundingSources) || CollectionHelper.isEmpty(fundingSourceLessors))
			return null;
		List<EconomicNature> economicNatures = new ArrayList<EconomicNature>();
		fundingSources.forEach(fundingSource -> {
			EconomicNature economicNature = null;
			if(fundingSource != null)
				for(FundingSourceLessor fundingSourceLessor : fundingSourceLessors) {
					if(fundingSource.getIdentifier().equals(fundingSourceLessor.getFundingSource().getIdentifier())) {
						economicNature = fundingSourceLessor.getEconomicNature();
						break;
					}
				}
			economicNatures.add(economicNature);
		});
		return economicNatures;
	}
}