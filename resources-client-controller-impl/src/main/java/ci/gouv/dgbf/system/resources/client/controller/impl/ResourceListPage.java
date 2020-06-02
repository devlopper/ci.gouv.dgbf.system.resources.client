package ci.gouv.dgbf.system.resources.client.controller.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.value.Value;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractDataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.output.OutputText;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityListPageContainerManagedImpl;

import ci.gouv.dgbf.system.resources.client.controller.entities.Amounts;
import ci.gouv.dgbf.system.resources.client.controller.entities.Resource;
import ci.gouv.dgbf.system.resources.server.persistence.api.query.ResourceQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class ResourceListPage extends AbstractEntityListPageContainerManagedImpl<Resource> implements Serializable {

	@Override
	protected DataTable __buildDataTable__() {
		DataTable dataTable = instantiateDataTable();
		@SuppressWarnings("unchecked")
		LazyDataModel<Resource> lazyDataModel = (LazyDataModel<Resource>) dataTable.getValue();
		lazyDataModel.setReadQueryIdentifier(ResourceQuerier.QUERY_IDENTIFIER_READ_VIEW_01);
		return dataTable;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Les lignes de recette du collectif budgétaire N° 1 de l'exercice 2020";
	}
	
	/**/
	
	@SuppressWarnings("unchecked")
	public static DataTable instantiateDataTable(Collection<String> columnsFieldsNames,DataTableListenerImpl listener,LazyDataModelListenerImpl lazyDataModelListener) {
		if(listener == null)
			listener = new DataTableListenerImpl();
		if(columnsFieldsNames == null) {
			columnsFieldsNames = CollectionHelper.listOf(Resource.FIELD_SECTION_AS_STRING,Resource.FIELD_BUDGET_SPECIALIZATION_UNIT_AS_STRING
					,Resource.FIELD_ACTIVITY_AS_STRING,Resource.FIELD_ECONOMIC_NATURE_AS_STRING,Resource.FIELD_AMOUNTS+"."+Amounts.FIELD_INITIAL);
		}
		
		DataTable dataTable = DataTable.build(DataTable.FIELD_LAZY,Boolean.TRUE,DataTable.FIELD_ELEMENT_CLASS,Resource.class
				,DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES,columnsFieldsNames,DataTable.FIELD_LISTENER,listener/*,DataTable.FIELD_STICKY_HEADER,Boolean.TRUE*/);
		dataTable.getOrderNumberColumn().setWidth("20");
		
		LazyDataModel<Resource> lazyDataModel = (LazyDataModel<Resource>) dataTable.getValue();
		lazyDataModel.setReaderUsable(Boolean.TRUE);
		if(lazyDataModelListener == null) {
			lazyDataModelListener = new LazyDataModelListenerImpl();
			lazyDataModel.setReadQueryIdentifier(ResourceQuerier.QUERY_IDENTIFIER_READ_VIEW_01);
		}
		lazyDataModel.setListener(lazyDataModelListener);
		return dataTable;
	}
	
	public static DataTable instantiateDataTable() {
		return instantiateDataTable(null, null, null);
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class DataTableListenerImpl extends DataTable.Listener.AbstractImpl implements Serializable {
		
		private Boolean initialAmountEditable,amountsColumnsFootersShowable;
		
		@Override
		public Map<Object, Object> getColumnArguments(AbstractDataTable dataTable, String fieldName) {
			Map<Object, Object> map = super.getColumnArguments(dataTable, fieldName);
			map.put(Column.ConfiguratorImpl.FIELD_EDITABLE, Boolean.FALSE);
			if(Resource.FIELD_SECTION_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Section");
			}else if(Resource.FIELD_BUDGET_SPECIALIZATION_UNIT_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Unité de spécialisation du budget");
			}else if(Resource.FIELD_ACTIVITY_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Activité");
			}else if(Resource.FIELD_ECONOMIC_NATURE_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Nature économique");
				map.put(Column.FIELD_FOOTER_OUTPUT_TEXT, OutputText.build(OutputText.FIELD_VALUE,"Total",OutputText.FIELD_STYLE,"float:right;font-weight: bold;"));
				map.put(Column.FIELD_LISTENER, new Column.Listener.AbstractImpl() {
					@Override
					public void setFooterValueFromMaster(Column column, Object master) {
						
					}
				});
			}else if((Resource.FIELD_AMOUNTS+"."+Amounts.FIELD_INITIAL).equals(fieldName)) {
				map.put(Column.FIELD_VALUE_TYPE, Value.Type.CURRENCY);
				map.put(Column.FIELD_HEADER_TEXT, "Montant");
				map.put(Column.ConfiguratorImpl.FIELD_EDITABLE, initialAmountEditable);
				map.put(Column.ConfiguratorImpl.FIELD_SHOW_FOOTER, amountsColumnsFootersShowable);
			}
			return map;
		}
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends LazyDataModel.Listener.AbstractImpl<Resource> implements Serializable {
			
	}
}