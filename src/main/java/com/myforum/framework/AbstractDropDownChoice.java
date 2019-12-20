package com.myforum.framework;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.PropertyModel;

import com.myforum.application.DBHelper;
import com.myforum.tables.AVKTable;

public abstract class AbstractDropDownChoice implements Serializable{
	private static final long serialVersionUID = 1L;
	protected AVKTable table;
	protected String tableColumnName;
	protected String wicketId;
	protected boolean isAutoSave=false;
	
	public AbstractDropDownChoice(String wicketId, final AVKTable table, final String tableColumnName, boolean isAutoSave){
		this.table = table; 						// e.g. Product
		this.tableColumnName = tableColumnName;		// e.g. "company" in Product
		this.wicketId = wicketId;					// e.g. "companies"
		this.isAutoSave = isAutoSave;				// whether a change to the dropdown should automatically be saved or not
	}
	
	/*
	 *  override this function to give saving instructions after setting/updating the table object
	 */
	protected abstract boolean update(AVKTable newSelection);

	/*
	 *  override this function to provide list of items in dropdown
	 */
	protected abstract List<? extends AVKTable> getList();

	/*
	 * Creates a DropDownChoice object with items from a list of "table"'s content
	 */
	public DropDownChoice<AVKTable> create(){
		DropDownChoice<AVKTable> ddc =
		new DropDownChoice<AVKTable>( wicketId, new PropertyModel<AVKTable>( table, tableColumnName ), (List<? extends AVKTable>) getList() ){
			private static final long serialVersionUID = 1L;
			
			protected boolean wantOnSelectionChangedNotifications(){
				return true;
			}
			
			@Override
			protected void onSelectionChanged( final AVKTable newSelection ){
	        	// Because we want to update the value immediately and don't want to wait until the page renders again, force an Ajax onUpdate event
	        	save( newSelection );
			}
		};

		// Need a choice renderer to be able to set the selected item in the list 
		ddc.setChoiceRenderer( (IChoiceRenderer<? super AVKTable>) new IChoiceRenderer<AVKTable>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Object getDisplayValue( AVKTable avkTable ) {	
				return avkTable.toString();
			}
			
			@Override
			public String getIdValue( AVKTable avkTable, int index ) {
				return String.valueOf( avkTable.getCode() );
			}
								
		});	
		
		return ddc;
	}

	private boolean save(AVKTable newSelection){
		update(newSelection);
		if (isAutoSave){
			return (DBHelper.saveAndCommit(table) != null); // saves the trouble of scrolling back and pressing save !
		}
		return true;
	}
}
