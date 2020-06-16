package ci.gouv.dgbf.system.resources.client.controller.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.controller.EntitySaver;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.log.LogHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.__kernel__.persistence.query.QueryExecutorArguments;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.__kernel__.user.interface_.message.RenderType;
import org.cyk.utility.__kernel__.value.ValueHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.JsfController;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.ajax.Ajax;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoice;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoiceOne;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AutoComplete;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.output.OutputText;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.panel.Dialog;

import ci.gouv.dgbf.system.resources.client.controller.entities.Activity;
import ci.gouv.dgbf.system.resources.client.controller.entities.Amounts;
import ci.gouv.dgbf.system.resources.client.controller.entities.BudgetSpecializationUnit;
import ci.gouv.dgbf.system.resources.client.controller.entities.BudgetSpecializationUnitCategory;
import ci.gouv.dgbf.system.resources.client.controller.entities.BudgetaryActVersion;
import ci.gouv.dgbf.system.resources.client.controller.entities.Resource;
import ci.gouv.dgbf.system.resources.client.controller.entities.Section;
import ci.gouv.dgbf.system.resources.server.business.api.ActivityBusiness;
import ci.gouv.dgbf.system.resources.server.business.api.ResourceBusiness;
import ci.gouv.dgbf.system.resources.server.persistence.api.query.ResourceByActivityQuerier;
import ci.gouv.dgbf.system.resources.server.persistence.api.query.SectionQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ResourceEditInitialsPage extends AbstractPageContainerManagedImpl implements Serializable {
				
	private BudgetaryActVersion budgetaryActVersion;
	private Collection<Section> sections;
	
	private Layout layout;
	private SelectOneCombo sectionSelectOne,budgetSpecializationUnitSelectOne,activitySelectOne;
	private AutoComplete activityAutoComplete;
	private DataTable resourcesDataTable;
	private CommandButton saveCommandButton,computeCommandButton,showActivitySearchDialogCommandButton;
	private Activity selectedActivity;
	private Resource selectedResource;
	private Collection<Resource> resources;
	private Map<String,Long> initialValues = new HashMap<>();
	private String budgetSpecializationUnitCategoriesDashboardOutputPanelIdentifier ="budgetSpecializationUnitCategoriesDashboardOutputPanel";
	private Dialog activitySearchDialog;
	
	@Override
	protected void __listenPostConstruct__() {
		try {
			budgetaryActVersion = WebController.getInstance().getRequestParameterEntityAsParent(BudgetaryActVersion.class);
		} catch (Exception exception) {
			LogHelper.log(exception, getClass());
			return;
		}
		if(budgetaryActVersion == null) {
			JsfController.getInstance().redirect("budgetaryActVersionSelectView",Map.of(ParameterName.ACTION_IDENTIFIER.getValue(),List.of(ResourceBusiness.SAVE_INITIALS)));
			return;
		}
		super.__listenPostConstruct__();
		sections = EntityReader.getInstance().readMany(Section.class, new Arguments<Section>()
				.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
						.setQueryExecutorArguments(new QueryExecutorArguments.Dto()
								.setQueryIdentifier(SectionQuerier.QUERY_IDENTIFIER_READ_AGGREGATION_BY_BUDGETARY_ACT_VERSION_CODE_BY_GET_ALL_BY_GET_TREE_ORDER_BY_CODE_ASCENDING)
								.addFilterField(SectionQuerier.PARAMETER_NAME_BUDGETARY_ACT_VERSION_CODE, budgetaryActVersion.getCode())
								)));
		
		selectedResource = WebController.getInstance().getRequestParameterEntity(Resource.class);
		if(selectedResource == null)
			selectedActivity = WebController.getInstance().getRequestParameterEntityAsParent(Activity.class);
		
		resourcesDataTable = instantiateActivityCostUnitsDataTable(CollectionHelper.listOf(Resource.FIELD_ECONOMIC_NATURE_AS_STRING
				,Resource.FIELD_AMOUNTS+"."+Amounts.FIELD_INITIAL
			),new LazyDataModelListenerImpl() {
			@Override
			public Arguments<Resource> instantiateArguments(LazyDataModel<Resource> lazyDataModel) {
				if(activitySelectOne == null || activitySelectOne.getValue() == null)
					return null;
				Activity activity = (Activity) activitySelectOne.getValue();
				QueryExecutorArguments.Dto queryExecutorArguments = new QueryExecutorArguments.Dto()
						.addFilterField(ResourceByActivityQuerier.PARAMETER_NAME_ACTIVITY_CODE, activity.getCode())
						.addFilterField(ResourceByActivityQuerier.PARAMETER_NAME_BUDGETARY_ACT_VERSION_CODE, budgetaryActVersion.getCode());
				queryExecutorArguments.setQueryIdentifier(ResourceByActivityQuerier.QUERY_IDENTIFIER_READ_BY_ACTIVITY_CODE_BY_BUDGETARY_ACT_VERSION_CODE);				
				return new Arguments<Resource>()
						.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments().setQueryExecutorArguments(queryExecutorArguments));
			}
		});
		
		activitySelectOne = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICE_CLASS,Activity.class,SelectOneCombo.FIELD_LISTENER,new SelectOneCombo.Listener.AbstractImpl<Activity>() {
			@Override
			public Collection<Activity> computeChoices(AbstractInputChoice<Activity> inputChoice) {
				if(budgetSpecializationUnitSelectOne == null || budgetSpecializationUnitSelectOne.getValue() ==  null)
					return null;
				return ((BudgetSpecializationUnit)budgetSpecializationUnitSelectOne.getValue()).getActivities();
			}
			
			@Override
			public Object getChoiceLabel(AbstractInputChoice<Activity> input, Activity activity) {
				if(activity == null)
					return null;
				return activity+" ("+NumberHelper.format(activity.getAmounts(Boolean.TRUE).getInitial())+")";
			}
			
			@Override
			public void select(AbstractInputChoiceOne input, Activity activity) {
				super.select(input, activity);
				if(resourcesDataTable != null) {					
					resourcesDataTable.setColumnsFootersValuesFromMaster(activity);
					resourcesDataTable.updateColumnsFooters();					
				}						
			}
		});
		activitySelectOne.enableValueChangeListener(List.of(resourcesDataTable));
		
		activitySearchDialog = Dialog.build(Dialog.FIELD_HEADER,"Recherche d'une activité",Dialog.FIELD_MODAL,Boolean.TRUE
				,Dialog.ConfiguratorImpl.FIELD_COMMAND_BUTTONS_BUILDABLE,Boolean.FALSE);
		activitySearchDialog.addStyleClasses("cyk-min-width-90-percent");
		showActivitySearchDialogCommandButton = CommandButton.build(CommandButton.FIELD_VALUE,"Rechercher",CommandButton.FIELD_ICON,"fa fa-search",CommandButton.FIELD_PROCESS,"@this"
				,CommandButton.FIELD___DIALOG__,activitySearchDialog,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.SHOW_DIALOG);
		
		activityAutoComplete = AutoComplete.build(AutoComplete.FIELD_ENTITY_CLASS,Activity.class,AutoComplete.FIELD_LISTENER,new AutoComplete.Listener.AbstractImpl<Activity>() {
			@Override
			public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
				
			}
		},AutoComplete.FIELD_PLACEHOLDER,"veuillez saisir une partie du code ou du libellé de l'activité");
		
		activityAutoComplete.enableAjaxItemSelect();
		activityAutoComplete.getAjaxes().get("itemSelect").setListener(new Ajax.Listener.AbstractImpl() {
			@Override
			protected void run(AbstractAction action) {
				Activity activity = (Activity) FieldHelper.read(action.get__argument__(), "source.value");
				if(activity != null)
					JsfController.getInstance().redirect("resourceEditInitialsView",Map.of(ParameterName.stringify(Activity.class),List.of(activity.getIdentifier())
							,ParameterName.stringify(BudgetaryActVersion.class),List.of(budgetaryActVersion.getIdentifier())));
			}
		});
		activityAutoComplete.getAjaxes().get("itemSelect").setDisabled(Boolean.FALSE);
		
		budgetSpecializationUnitSelectOne = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICE_CLASS,BudgetSpecializationUnit.class,SelectOneCombo.FIELD_LISTENER,new SelectOneCombo.Listener.AbstractImpl<BudgetSpecializationUnit>() {
			@Override
			public Collection<BudgetSpecializationUnit> computeChoices(AbstractInputChoice<BudgetSpecializationUnit> inputChoice) {
				if(sectionSelectOne == null || sectionSelectOne.getValue() ==  null)
					return null;
				return ((Section)sectionSelectOne.getValue()).getBudgetSpecializationUnits();
			}
			
			@Override
			public void select(AbstractInputChoiceOne input, BudgetSpecializationUnit budgetSpecializationUnit) {
				super.select(input, budgetSpecializationUnit);
				if(activitySelectOne != null) {					
					activitySelectOne.setChoicesInitialized(Boolean.TRUE);
					activitySelectOne.updateChoices();
					if(CollectionHelper.isEmpty(activitySelectOne.getChoices())) {
						activitySelectOne.select(null);
					}else {
						if(selectedResource != null)
							activitySelectOne.selectBySystemIdentifier(selectedResource.getActivity().getIdentifier());
						else if(selectedActivity != null)
							activitySelectOne.selectBySystemIdentifier(selectedActivity.getIdentifier());
						else
							activitySelectOne.selectFirstChoice();	
					}
					
				}
			}
		});
		budgetSpecializationUnitSelectOne.enableValueChangeListener(List.of(activitySelectOne,resourcesDataTable));
		
		sectionSelectOne = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICE_CLASS,Section.class,SelectOneCombo.FIELD_CHOICES,sections
				,SelectOneCombo.FIELD_LISTENER,new SelectOneCombo.Listener.AbstractImpl<Section>() {	
			@Override
			public void select(AbstractInputChoiceOne input, Section section) {
				super.select(input, section);				
				if(section == null) {
					
				}else {
					//budgetSpecializationUnitCategoriesBySection = section.getBudgetSpecializationUnitCategories();
					/*Collection<BudgetSpecializationUnitCategory> _budgetSpecializationUnitCategoriesBySection_ = EntityReader.getInstance().readMany(BudgetSpecializationUnitCategory.class,new Arguments<BudgetSpecializationUnitCategory>()
							.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
									.setQueryExecutorArguments(new QueryExecutorArguments.Dto()
											.setQueryIdentifier(BudgetSpecializationUnitCategoryQuerier.QUERY_IDENTIFIER_READ_AGGREGATION_BY_SECTIONS_CODES_BY_TYPES_CODES_BY_BUDGETARY_ACT_VERSION_CODE_ORDER_BY_CODE_ASCENDING)
											.addFilterField(BudgetSpecializationUnitCategoryQuerier.PARAMETER_NAME_SECTIONS_CODES, List.of(section.getCode()))
											.addFilterField(BudgetSpecializationUnitCategoryQuerier.PARAMETER_NAME_TYPES_CODES
													, List.of(ci.gouv.dgbf.system.resources.server.persistence.entities.BudgetSpecializationUnitType.CODE_RESOURCE))
											.addFilterField(BudgetSpecializationUnitCategoryQuerier.PARAMETER_NAME_BUDGETARY_ACT_VERSION_CODE, budgetaryActVersion.getCode())
											)));
					
					if(budgetSpecializationUnitCategoriesBySection == null)
						budgetSpecializationUnitCategoriesBySection = new ArrayList<>();
					else
						budgetSpecializationUnitCategoriesBySection.clear();
					BudgetSpecializationUnitCategory budgetSpecializationUnitCategory;
					for(BudgetSpecializationUnitCategory index : budgetSpecializationUnitCategories) {
						budgetSpecializationUnitCategory = null;
						if(CollectionHelper.isNotEmpty(_budgetSpecializationUnitCategoriesBySection_))
							for(BudgetSpecializationUnitCategory i : _budgetSpecializationUnitCategoriesBySection_)
								if(i.getIdentifier().equals(index.getIdentifier())) {
									budgetSpecializationUnitCategory = i;
									break;
								}
						if(budgetSpecializationUnitCategory == null) {
							budgetSpecializationUnitCategory = new BudgetSpecializationUnitCategory();
							budgetSpecializationUnitCategory.setCode(index.getCode());
							budgetSpecializationUnitCategory.setName(index.getName());
							budgetSpecializationUnitCategory.setAsString(index.getCode()+" "+index.getName());
						}
						budgetSpecializationUnitCategoriesBySection.add(budgetSpecializationUnitCategory);
					}
					if(budgetSpecializationUnitCategoryBySectionTotal == null) {
						budgetSpecializationUnitCategoryBySectionTotal = new BudgetSpecializationUnitCategory();
						budgetSpecializationUnitCategoryBySectionTotal.setCode("Totaux");
						budgetSpecializationUnitCategoryBySectionTotal.setName("");
					}
					budgetSpecializationUnitCategoryBySectionTotal.setAmounts(section.getAmounts());
					budgetSpecializationUnitCategoriesBySection.add(budgetSpecializationUnitCategoryBySectionTotal);
					*/
				}
				
				if(budgetSpecializationUnitSelectOne != null) {
					budgetSpecializationUnitSelectOne.setChoicesInitialized(Boolean.TRUE);
					budgetSpecializationUnitSelectOne.updateChoices();
					if(selectedResource != null)
						budgetSpecializationUnitSelectOne.selectBySystemIdentifier(selectedResource.getActivity().getBudgetSpecializationUnit().getIdentifier());
					else if(selectedActivity != null)
						budgetSpecializationUnitSelectOne.selectBySystemIdentifier(selectedActivity.getBudgetSpecializationUnit().getIdentifier());
					else
						budgetSpecializationUnitSelectOne.selectFirstChoice();
				}
			}
		});
		
		if(selectedResource != null) {
			sectionSelectOne.selectBySystemIdentifier(selectedResource.getActivity().getBudgetSpecializationUnit().getSection().getIdentifier());
		}else if(selectedActivity != null) {
			sectionSelectOne.selectBySystemIdentifier(selectedActivity.getBudgetSpecializationUnit().getSection().getIdentifier());
		}else {
			sectionSelectOne.select((Section) CollectionHelper.getFirst(sectionSelectOne.getChoices().stream().filter(object -> FieldHelper.readBusinessIdentifier(object).equals("322")).collect(Collectors.toList())));
		}
		sectionSelectOne.enableValueChangeListener(List.of(budgetSpecializationUnitSelectOne,activitySelectOne,resourcesDataTable,budgetSpecializationUnitCategoriesDashboardOutputPanelIdentifier));
		
		saveCommandButton = CommandButton.build(CommandButton.FIELD_VALUE,"Enregistrer",CommandButton.FIELD_ICON,"fa fa-floppy-o",CommandButton.FIELD_STYLE,"float:right;"
				,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION
				,CommandButton.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_RENDER_TYPES,List.of(RenderType.GROWL));
		saveCommandButton.addUpdates(":form:"+activitySelectOne.getIdentifier(),":form:"+budgetSpecializationUnitCategoriesDashboardOutputPanelIdentifier,":form:"+resourcesDataTable.getIdentifier());
		saveCommandButton.setListener(new CommandButton.Listener.AbstractImpl() {
			@Override
			public void run(AbstractAction action) {
				if(CollectionHelper.isNotEmpty(resources)) {
					Collection<Resource> updatables = null;
					for(Resource resource : resources) {
						Long initialValue = initialValues.get(resource.getIdentifier());
						Long adjustment = resource.getAmounts(Boolean.TRUE).getInitial();
						if((initialValue == null && adjustment == null || initialValue != null && initialValue.equals(adjustment)))							
							continue;
						if(updatables == null)
							updatables = new ArrayList<>();
						updatables.add(resource);
					}
					if(CollectionHelper.isNotEmpty(updatables)) {					
						updatables.forEach(resource -> {
							if(resource.getAmounts(Boolean.TRUE).getInitial() == null)
								resource.getAmounts(Boolean.TRUE).setInitial(0l);
						});
						EntitySaver.getInstance().save(Resource.class, new Arguments<Resource>().setUpdatables(updatables)
								.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments().setActionIdentifier(ResourceBusiness.SAVE_INITIALS)));
						Long total = 0l;
						for(Resource resource : resources)
							total = total + ValueHelper.defaultToIfBlank(resource.getAmounts(Boolean.TRUE).getInitial(),0l);
						Long gap = ((Activity)activitySelectOne.getValue()).getAmounts().getInitial() - total;
						((Activity)activitySelectOne.getValue()).getAmounts().setInitial(total);
						Collection<BudgetSpecializationUnitCategory> budgetSpecializationUnitCategories = ((Section)sectionSelectOne.getValue()).getBudgetSpecializationUnitCategories();
						BudgetSpecializationUnitCategory category = CollectionHelper.getFirst(budgetSpecializationUnitCategories.stream().filter(i -> i.getIdentifier()!= null 
								&& i.getIdentifier().equals((((BudgetSpecializationUnit)budgetSpecializationUnitSelectOne.getValue()).getCategory().getIdentifier()))).collect(Collectors.toList()));					
						category.getAmounts(Boolean.TRUE).setInitial(category.getAmounts(Boolean.TRUE).getInitial()-gap);
						//budgetSpecializationUnitCategoryBySectionTotal.getAmounts(Boolean.TRUE).setInitial(budgetSpecializationUnitCategoryBySectionTotal.getAmounts(Boolean.TRUE).getInitial()-gap);
						
						resourcesDataTable.setColumnsFootersValuesFromMaster(activitySelectOne.getValue());
						resourcesDataTable.updateColumnsFooters();						
					}					
				}
			}
		});
		
		computeCommandButton = CommandButton.build(CommandButton.FIELD_VALUE,"Calculer",CommandButton.FIELD_ICON,"fa fa-floppy-o",CommandButton.FIELD_STYLE,"float:right;"
				,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION
				,CommandButton.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_RENDER_TYPES,List.of(RenderType.GROWL));
		computeCommandButton.addUpdates(":form:"+activitySelectOne.getIdentifier(),":form:"+budgetSpecializationUnitCategoriesDashboardOutputPanelIdentifier,":form:"+resourcesDataTable.getIdentifier());
		computeCommandButton.setListener(new CommandButton.Listener.AbstractImpl() {
			@Override
			public void run(AbstractAction action) {
				if(activitySelectOne != null && activitySelectOne.getValue() != null) {
					Activity activity = (Activity)activitySelectOne.getValue();
					activity.setBudgetaryActVersion(budgetaryActVersion);
					EntitySaver.getInstance().save(Activity.class, new Arguments<Activity>().setUpdatables(List.of(activity))
						.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments().setActionIdentifier(ActivityBusiness.SAVE_INITIALS_FROM_COMPUTATION)));										
				}
			}
		});
		
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,CollectionHelper.listOf(
				MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Section"),Cell.FIELD_WIDTH,2),MapHelper.instantiate(Cell.FIELD_CONTROL,sectionSelectOne,Cell.FIELD_WIDTH,10)	
				,MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue(""),Cell.FIELD_WIDTH,2),MapHelper.instantiate(Cell.FIELD_IDENTIFIER,"section_dashboard",Cell.FIELD_WIDTH,10)
				,MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Spécialisation"),Cell.FIELD_WIDTH,2),MapHelper.instantiate(Cell.FIELD_CONTROL,budgetSpecializationUnitSelectOne,Cell.FIELD_WIDTH,10)
				,MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Activité"),Cell.FIELD_WIDTH,2),MapHelper.instantiate(Cell.FIELD_CONTROL,activitySelectOne,Cell.FIELD_WIDTH,9)
				,MapHelper.instantiate(Cell.FIELD_CONTROL,showActivitySearchDialogCommandButton,Cell.FIELD_WIDTH,1)
				,MapHelper.instantiate(Cell.FIELD_CONTROL,resourcesDataTable,Cell.FIELD_WIDTH,12)
				,MapHelper.instantiate(Cell.FIELD_CONTROL,saveCommandButton,Cell.FIELD_WIDTH,12)
				,MapHelper.instantiate(Cell.FIELD_CONTROL,computeCommandButton,Cell.FIELD_WIDTH,12)
				
			));
		
		selectedActivity = null;
		selectedResource = null;
	}
	
	@SuppressWarnings("unchecked")
	private DataTable instantiateActivityCostUnitsDataTable(Collection<String> columnsFieldsNames,LazyDataModelListenerImpl lazyDataModelListener) {		
		DataTable dataTable = ResourceListPage.instantiateDataTable(columnsFieldsNames
				, new ResourceListPage.DataTableListenerImpl().setAmountsColumnsFootersShowable(Boolean.TRUE).setInitialAmountEditable(Boolean.TRUE)
				,lazyDataModelListener );
				
		dataTable.setRows(Integer.MAX_VALUE);
		dataTable.addHeaderToolbarLeftCommandsByArgumentsOpenViewInDialogCreate(CommandButton.FIELD_VALUE, "Ajouter",CommandButton.FIELD_PROCESS, "@this",CommandButton.FIELD_LISTENER
			,new CommandButton.Listener.AbstractImpl() {
				@Override
				protected Map<String, List<String>> getViewParameters(AbstractAction action) {
					Map<String, List<String>> parameters = super.getViewParameters(action);
					if(activitySelectOne != null && activitySelectOne.getValue() != null) {
						if(parameters == null)
							parameters = new HashMap<>();
						parameters.put(ParameterName.stringify(Activity.class), List.of(((Activity)activitySelectOne.getValue()).getIdentifier()));
					}					
					return parameters;
				}
				
				@Override
				protected String getOutcome(AbstractAction action) {
					return "resourceCreateView";
				}
			});
		
		LazyDataModel<Resource> lazyDataModel = (LazyDataModel<Resource>) dataTable.getValue();
		lazyDataModel.setReaderUsable(Boolean.TRUE);
		lazyDataModel.setIsCountEqualsListSize(Boolean.TRUE);
		return dataTable;
	}
	
	public void validateInitial(FacesContext context, UIComponent component,Object value) throws ValidatorException {
		try {
			ResourceBusiness.validateInitial((Long)value);
		} catch (Exception exception) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,exception.getMessage(),exception.getMessage()));	
		}			
	}

	@Override
	protected String __getWindowTitleValue__() {
		return "Saisie des Resources - "+(budgetaryActVersion == null ? "Aucune version trouveé" : budgetaryActVersion.getBudgetaryAct().getName()+" - "+budgetaryActVersion.getName());
	}	
	
	/**/
	
	public class LazyDataModelListenerImpl extends ResourceListPage.LazyDataModelListenerImpl implements Serializable {
		
		@Override
		public Arguments<Resource> instantiateArguments(LazyDataModel<Resource> lazyDataModel) {
			if(activitySelectOne == null || activitySelectOne.getValue() == null)
				return null;
			Activity activity = (Activity) activitySelectOne.getValue();
			QueryExecutorArguments.Dto queryExecutorArguments = new QueryExecutorArguments.Dto()
					.addFilterField(ResourceByActivityQuerier.PARAMETER_NAME_ACTIVITIES_CODES, List.of(activity.getCode()))
					.addFilterField(ResourceByActivityQuerier.PARAMETER_NAME_BUDGETARY_ACT_VERSION_CODE, budgetaryActVersion.getCode())
					;
			queryExecutorArguments.setQueryIdentifier(ResourceByActivityQuerier.QUERY_IDENTIFIER_READ_BY_ACTIVITIES_CODES_BY_BUDGETARY_ACT_VERSION_CODE);				
			return new Arguments<Resource>()
					.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments().setQueryExecutorArguments(queryExecutorArguments));
		}
		
		@Override
		public List<Resource> read(LazyDataModel<Resource> lazyDataModel) {
			if(lazyDataModel.get__readerArguments__() == null)
				return null;
			List<Resource> list = super.read(lazyDataModel);
			resources = list;
			initialValues.clear();
			if(CollectionHelper.isNotEmpty(resources))
				list.forEach(resource -> {
					initialValues.put(resource.getIdentifier(), resource.getAmounts(Boolean.TRUE).getInitial());
				});
			return list;
		}
	}
}