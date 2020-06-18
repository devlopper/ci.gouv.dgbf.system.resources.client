package ci.gouv.dgbf.system.resources.client.controller.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.controller.EntitySaver;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.persistence.query.QueryExecutorArguments;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.__kernel__.user.interface_.message.RenderType;
import org.cyk.utility.client.controller.web.jsf.converter.Converter;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractDataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;

import ci.gouv.dgbf.system.resources.client.controller.entities.Activity;
import ci.gouv.dgbf.system.resources.client.controller.entities.BudgetaryActVersion;
import ci.gouv.dgbf.system.resources.client.controller.entities.EconomicNature;
import ci.gouv.dgbf.system.resources.client.controller.entities.FundingSource;
import ci.gouv.dgbf.system.resources.client.controller.entities.FundingSourceLessor;
import ci.gouv.dgbf.system.resources.client.controller.entities.Lessor;
import ci.gouv.dgbf.system.resources.server.business.api.FundingSourceLessorBusiness;
import ci.gouv.dgbf.system.resources.server.persistence.api.query.EconomicNatureQuerier;
import ci.gouv.dgbf.system.resources.server.persistence.api.query.FundingSourceQuerier;
import ci.gouv.dgbf.system.resources.server.persistence.api.query.LessorQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class FundingSourceLessorEditEconomicNaturesPage extends AbstractPageContainerManagedImpl implements Serializable {

	private Layout layout;
	private Map<String,FundingSource> fundingSourcesMap;
	private Collection<EconomicNature> economicNatures;
	private List<FundingSource> fundingSources;
	private Collection<Lessor> lessors;
	private DataTable lessorsDataTable;
	private BudgetaryActVersion budgetaryActVersion;
	private Activity activity;
	private CommandButton saveCommandButton;
	private Map<String,List<EconomicNature>> initialEconomicNatures;
	private Converter economicNatureConverter = new Converter();
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		lessors = EntityReader.getInstance().readMany(Lessor.class, new Arguments<Lessor>()
				.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
						.setQueryExecutorArguments(new QueryExecutorArguments.Dto()
								.setQueryIdentifier(LessorQuerier.QUERY_IDENTIFIER_READ_ALL_WITH_ALL_FUNDING_SOURCES_ORDER_BY_CODE_ASCENDING))));
		
		fundingSources = (List<FundingSource>) EntityReader.getInstance().readMany(FundingSource.class, new Arguments<FundingSource>()
				.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
						.setQueryExecutorArguments(new QueryExecutorArguments.Dto()
								.setQueryIdentifier(FundingSourceQuerier.QUERY_IDENTIFIER_READ_BY_CODES_ORDER_BY_CODE_ASCENDING)
								.addFilterField(FundingSourceQuerier.PARAMETER_CODES,ci.gouv.dgbf.system.resources.server.persistence.entities.FundingSource.CATEGORY_EXTERNAL_CODES))));
		
		initialEconomicNatures = new HashMap<>();
		if(CollectionHelper.isNotEmpty(lessors))
			lessors.forEach(lessor -> {
				List<EconomicNature> economicNatures = lessor.getEconomicNatures(fundingSources);
				lessor.setDonEconomicNature(CollectionHelper.getElementAt(economicNatures, 0));
				lessor.setEmpruntEconomicNature(CollectionHelper.getElementAt(economicNatures, 1));
				initialEconomicNatures.put(lessor.getIdentifier(), economicNatures);
			});
		
		List<String> columnsFieldsNames = new ArrayList<>();
		columnsFieldsNames.addAll(List.of(Lessor.FIELD_CODE,Lessor.FIELD_NAME));
		
		if(CollectionHelper.isNotEmpty(fundingSources)) {
			fundingSourcesMap = new LinkedHashMap<>(); 
			fundingSources.forEach(fundingSource -> {
				fundingSourcesMap.put(fundingSource.getCode(), fundingSource);
				columnsFieldsNames.add(fundingSource.getCode());
			});
			
			economicNatures = EntityReader.getInstance().readMany(EconomicNature.class, new Arguments<EconomicNature>()
					.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
							.setQueryExecutorArguments(new QueryExecutorArguments.Dto()
									.setQueryIdentifier(EconomicNatureQuerier.QUERY_IDENTIFIER_READ_ORDER_BY_CODE_ASCENDING))));
		}
		
		
			
		lessorsDataTable = DataTable.build(DataTable.FIELD_LAZY,Boolean.FALSE,DataTable.FIELD_ELEMENT_CLASS,Lessor.class,DataTable.FIELD_VALUE,lessors
			,DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES,columnsFieldsNames,DataTable.FIELD_LISTENER,new DataTable.Listener.AbstractImpl() {
			public Map<Object,Object> getColumnArguments(AbstractDataTable dataTable, String fieldName) {
				Map<Object, Object> map = super.getColumnArguments(dataTable, fieldName);
				map.put(Column.ConfiguratorImpl.FIELD_EDITABLE, Boolean.FALSE);
				if(Lessor.FIELD_CODE.equals(fieldName)) {
					map.put(Column.FIELD_HEADER_TEXT, "Code");
					map.put(Column.FIELD_WIDTH, "100");
				}else if(Lessor.FIELD_NAME.equals(fieldName)) {
					map.put(Column.FIELD_HEADER_TEXT, "Libellé");
				}else {
					map.put(Column.FIELD_HEADER_TEXT, fundingSourcesMap.get(fieldName).getName());
					map.put(Column.FIELD_WIDTH, "200");
				}
				return map;
			}
			
			public Object getCellValueByRecordByColumn(Object record, Integer recordIndex, Column column, Integer columnIndex) {
				if(Lessor.FIELD_CODE.equals(column.getFieldName()) || Lessor.FIELD_NAME.equals(column.getFieldName()))
					return super.getCellValueByRecordByColumn(record, recordIndex, column, columnIndex);
				return ((List<FundingSourceLessor>)((Lessor)record).getFundingSourceLessors(Boolean.TRUE)).get(columnIndex-2);
			}
		});
		
		saveCommandButton = CommandButton.build(CommandButton.FIELD_VALUE,"Enregistrer",CommandButton.FIELD_ICON,"fa fa-floppy-o",CommandButton.FIELD_STYLE,"float:right;"
				,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION
				,CommandButton.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_RENDER_TYPES,List.of(RenderType.GROWL));
		saveCommandButton.setListener(new CommandButton.Listener.AbstractImpl() {
			@Override
			public void run(AbstractAction action) {
				if(CollectionHelper.isNotEmpty(lessors)) {
					Arguments<FundingSourceLessor> arguments = new Arguments<FundingSourceLessor>()
							.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments().setActionIdentifier(FundingSourceLessorBusiness.SAVE_ECONOMIC_NATURES));
					for(Lessor lessor : lessors) {
						if(CollectionHelper.isEmpty(lessor.getFundingSourceLessors()))
							continue;
						List<EconomicNature> t0 = initialEconomicNatures.get(lessor.getIdentifier());
						Integer index = 0;
						for(FundingSourceLessor fundingSourceLessor : lessor.getFundingSourceLessors()) {
							if(index == 0)
								fundingSourceLessor.setEconomicNature(lessor.getDonEconomicNature());
							else if(index == 1)
								fundingSourceLessor.setEconomicNature(lessor.getEmpruntEconomicNature());
							EconomicNature initial = CollectionHelper.getElementAt(t0, index++);
							if(initial == null && fundingSourceLessor.getEconomicNature() == null || initial != null && initial.equals(fundingSourceLessor.getEconomicNature()))								
								continue;
							if(fundingSourceLessor.getEconomicNature() == null)
								arguments.addDeletables(fundingSourceLessor);
							else {
								fundingSourceLessor.setLessorIdentifier(lessor.getIdentifier());
								arguments.addCreatablesOrUpdatables(fundingSourceLessor);
							}
						}
					}
					if(CollectionHelper.isEmpty(arguments.getCreatables()) && CollectionHelper.isEmpty(arguments.getUpdatables()) && CollectionHelper.isEmpty(arguments.getDeletables())) {
						
					}else {
						EntitySaver.getInstance().save(FundingSourceLessor.class, arguments);		
					}					
				}			
			}
		});
		
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,CollectionHelper.listOf(
				MapHelper.instantiate(Cell.FIELD_CONTROL,lessorsDataTable,Cell.FIELD_WIDTH,12)
				,MapHelper.instantiate(Cell.FIELD_CONTROL,saveCommandButton,Cell.FIELD_WIDTH,12)			
			));
	}
	
	public Collection<EconomicNature> getEconomicNaturesByQuery(String query) {
		if(CollectionHelper.isEmpty(economicNatures))
			return null;
		if(StringHelper.isBlank(query))
			return economicNatures;
		return economicNatures.stream().filter(x -> x.getCode().startsWith(query)).collect(Collectors.toList());
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Mise à jour des comptes bailleurs";
	}	
}