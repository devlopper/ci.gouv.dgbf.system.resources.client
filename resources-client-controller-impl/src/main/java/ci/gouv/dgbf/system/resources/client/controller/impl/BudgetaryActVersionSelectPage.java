package ci.gouv.dgbf.system.resources.client.controller.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.constant.ConstantEmpty;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.persistence.query.QueryExecutorArguments;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.JsfController;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;

import ci.gouv.dgbf.system.resources.client.controller.entities.BudgetaryActVersion;
import ci.gouv.dgbf.system.resources.server.business.api.ResourceBusiness;
import ci.gouv.dgbf.system.resources.server.persistence.api.query.BudgetaryActVersionQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class BudgetaryActVersionSelectPage extends AbstractPageContainerManagedImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String actionIdentifier;
	private SelectOneCombo budgetaryActVersionSelectOneCombo;
	private Layout layout;
	
	@Override
	protected void __listenPostConstruct__() {
		actionIdentifier = WebController.getInstance().getRequestParameter(ParameterName.ACTION_IDENTIFIER.getValue());
		super.__listenPostConstruct__();
		try {
			budgetaryActVersionSelectOneCombo = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICE_CLASS,BudgetaryActVersion.class,SelectOneCombo.FIELD_CHOICES
					,EntityReader.getInstance().readMany(BudgetaryActVersion.class, new Arguments<BudgetaryActVersion>()
							.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
									.setQueryExecutorArguments(new QueryExecutorArguments.Dto().setQueryIdentifier(BudgetaryActVersionQuerier.QUERY_IDENTIFIER_READ_ALL_01)
											))) ,SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Version")/*.enableAjaxItemSelect()*/;
			//actionPlanAutoComplete = AutoComplete.build(AutoComplete.FIELD_ENTITY_CLASS,ActionPlan.class).useQueryIdentifiersFiltersLike().enableAjaxItemSelect().listenComplete(producerAutoComplete);
			//activityAutoComplete = AutoComplete.build(AutoComplete.FIELD_ENTITY_CLASS,Activity.class).useQueryIdentifiersFiltersLike().enableAjaxItemSelect().listenComplete(actionPlanAutoComplete);
			//costUnitAutoComplete = AutoComplete.build(AutoComplete.FIELD_ENTITY_CLASS,CostUnit.class).useQueryIdentifiersFiltersLike().enableAjaxItemSelect().listenComplete(activityAutoComplete);
			layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G
					,Layout.FIELD_NUMBER_OF_COLUMNS,2,Layout.FIELD_ROW_CELL_MODEL,Map.of(0,new Cell().setWidth(2),1,new Cell().setWidth(10))
					,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,CollectionHelper.listOf(
						MapHelper.instantiate(Cell.FIELD_CONTROL,budgetaryActVersionSelectOneCombo.getOutputLabel()),MapHelper.instantiate(Cell.FIELD_CONTROL,budgetaryActVersionSelectOneCombo)
						//,MapHelper.instantiate(Cell.FIELD_CONTROL,actionPlanAutoComplete.getOutputLabel()),MapHelper.instantiate(Cell.FIELD_CONTROL,actionPlanAutoComplete)
						//,MapHelper.instantiate(Cell.FIELD_CONTROL,activityAutoComplete.getOutputLabel()),MapHelper.instantiate(Cell.FIELD_CONTROL,activityAutoComplete)
						//,MapHelper.instantiate(Cell.FIELD_CONTROL,costUnitAutoComplete.getOutputLabel()),MapHelper.instantiate(Cell.FIELD_CONTROL,costUnitAutoComplete)
						
						,MapHelper.instantiate(Cell.ConfiguratorImpl.FIELD_CONTROL_COMMAND_BUTTON_ARGUMENTS,MapHelper.instantiate(CommandButton.ConfiguratorImpl.FIELD_OBJECT,this
								,CommandButton.ConfiguratorImpl.FIELD_METHOD_NAME,"select"))
					));
		}catch(Exception exception) {
			exception.printStackTrace();
		}
		
	}
	
	public void select() {
		if(budgetaryActVersionSelectOneCombo == null || budgetaryActVersionSelectOneCombo.getValue() == null)
			return;
		BudgetaryActVersion budgetaryActVersion = (BudgetaryActVersion) budgetaryActVersionSelectOneCombo.getValue();
		JsfController.getInstance().redirect("resourceEditInitialsView", Map.of(ParameterName.stringify(BudgetaryActVersion.class),List.of(budgetaryActVersion.getIdentifier())));						
	}
		
	@Override
	protected String __getWindowTitleValue__() {
		String suffix = ConstantEmpty.STRING;
		if(ResourceBusiness.SAVE_INITIALS.equals(actionIdentifier))
			suffix = " afin de saisir les montants";
		return "Choisir une version"+suffix;
	}
}