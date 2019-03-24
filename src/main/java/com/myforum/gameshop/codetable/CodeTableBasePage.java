package com.myforum.gameshop.codetable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.PropertyPopulator;
import org.apache.wicket.markup.html.link.StatelessLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.myforum.application.StringLogics;
import com.myforum.base.AVKPage;

public abstract class CodeTableBasePage extends AVKPage {
	private static final long serialVersionUID = 1L;

	/*
	 * Class responsible for preparing and populating the cells (columns in rows) in the grid with data
	 * using the data provided by the CodeTableProvider class
	 */
	protected abstract class CodeTablePopulator<T> extends PropertyPopulator<T>{
		private static final long serialVersionUID = 1L;

		protected String property = "";
		
		public CodeTablePopulator(String property) {
			super(property);
			this.property = property;
		}

		@Override
		public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, final IModel<T> rowModel) {
			final String description = getDescription(rowModel);
			
			StatelessLink<Object> link = new StatelessLink<Object>(componentId) {
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick() {
					rowClick(rowModel);
				}
			};
			
			link.setBody(Model.of(description));
			
			// check if the string is a numeric value... in that case, right align the label
			if (StringLogics.isNumeric(description)) {
				cellItem.add(new AttributeModifier("align", "right"));
			}
			
			cellItem.add(link);			
		}
		
		public abstract void rowClick(IModel<T> rowModel);
		
		/*
		 *  Use this function to cast the rowmodel to the applicable codetable in com.myforum.tables
		 *  and explain what description should show depending on the property (read: columnName) is requested
		 */
		public abstract String getDescription(IModel<T> rowModel);
	}

	/*
	 * Class responsible for providing a list of data from a table to be shown 
	 * in the grid, populated using the populator (CodeTablePopulator class)
	 */
	protected abstract class CodeTableProvider<T> implements IDataProvider<T>{
		private static final long serialVersionUID = 1L;
		protected List<T> list = new ArrayList<T>();
		
		@Override
		public void detach() {
		}

		@Override
		public Iterator<T> iterator(long arg_first, long arg_count) {
	        List<T> newList = new ArrayList<T>(list);
	        Collections.sort(newList, new Comparator<T>() {
	            @Override
	            public int compare(T t1, T t2)
	            {
	                return  compareRow( t1, t2 );
	            }
	        });

			int first = 0;
			int count = 0;
			try {
				first = (int) arg_first;
				count = (int) arg_count;
			}catch(Exception e) {
				//just dump the entire list 
				return newList.iterator();
			}	

	        // Return the data for the current page - this can be determined only after sorting
	        return newList.subList(first, first + count).iterator();
		}
	    
		public abstract int compareRow(T t1, T t2);
		
		@Override
		public long size() {
			return list.size();
		}
	}

}

