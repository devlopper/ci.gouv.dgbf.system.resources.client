package ci.gouv.dgbf.system.resources.client.controller.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.persistence.query.QueryExecutorArguments;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoice;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityEditPageContainerManagedImpl;

import ci.gouv.dgbf.system.resources.client.controller.entities.Activity;
import ci.gouv.dgbf.system.resources.client.controller.entities.BudgetSpecializationUnit;
import ci.gouv.dgbf.system.resources.client.controller.entities.BudgetaryActVersion;
import ci.gouv.dgbf.system.resources.client.controller.entities.EconomicNature;
import ci.gouv.dgbf.system.resources.client.controller.entities.Resource;
import ci.gouv.dgbf.system.resources.server.persistence.api.query.EconomicNatureQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ResourceCreatePage extends AbstractEntityEditPageContainerManagedImpl<Resource> implements Serializable {

	private BudgetSpecializationUnit budgetSpecializationUnit;
	private BudgetaryActVersion budgetaryActVersion;
	private Activity activity;
	
	@Override
	protected void __listenPostConstruct__() {
		budgetSpecializationUnit = WebController.getInstance().getRequestParameterEntityAsParent(BudgetSpecializationUnit.class);
		budgetaryActVersion = WebController.getInstance().getRequestParameterEntityAsParent(BudgetaryActVersion.class);
		activity = WebController.getInstance().getRequestParameterEntityAsParent(Activity.class);
		super.__listenPostConstruct__();		
		((Resource)form.getEntity()).setActivity(activity).setBudgetSpecializationUnit(budgetSpecializationUnit).setBudgetaryActVersion(budgetaryActVersion);
	}
	
	@Override
	protected Map<Object, Object> __getFormArguments__() {
		Map<Object, Object> arguments = super.__getFormArguments__();
		arguments.put(Form.ConfiguratorImpl.FIELD_LISTENER, new Form.ConfiguratorImpl.Listener.AbstractImpl() {
			@Override
			public Collection<String> getFieldsNames(Form form) {
				return CollectionHelper.listOf(Resource.FIELD_ECONOMIC_NATURE);
			}
			
			@Override
			public Map<Object, Object> getInputArguments(Form form, String fieldName) {
				Map<Object, Object> arguments = super.getInputArguments(form, fieldName);
				if(Resource.FIELD_ECONOMIC_NATURE.equals(fieldName))
					arguments.put(AbstractInputChoice.FIELD_LISTENER, new AbstractInputChoice.Listener.AbstractImpl<EconomicNature>() {
						@Override
						public Collection<EconomicNature> computeChoices(AbstractInputChoice<EconomicNature> input) {
							Collection<EconomicNature> costUnits = EntityReader.getInstance().readMany(EconomicNature.class, new Arguments<EconomicNature>()
									.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
											.setQueryExecutorArguments(new QueryExecutorArguments.Dto()
													.setQueryIdentifier(EconomicNatureQuerier.QUERY_IDENTIFIER_READ_WHERE_ACTIVITY_REVENUE_ECONOMIC_NATURE_DOES_NOT_EXIST_AND_IDENTIFIER_DOES_START_BY_7_BY_ACTIVITIES_CODES_ORDER_BY_CODE_ASCENDING)
													.addFilterField(EconomicNatureQuerier.PARAMETER_ACTIVITIES_CODES, List.of(activity.getIdentifier())))));
							return costUnits;
						}
					});
				return arguments;
			}
			
			@Override
			public Map<Object, Object> getCommandButtonArguments(Form form, Collection<AbstractInput<?>> inputs) {
				Map<Object, Object> arguments = super.getCommandButtonArguments(form, inputs);
				arguments.put(CommandButton.FIELD_VALUE, "Ajouter");
				return arguments;
			}
		});
		return arguments;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Ajout d'une nature économique à l'activitié "+activity;
	}	
}