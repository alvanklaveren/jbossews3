package com.myforum.gameshop.codetable;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.myforum.tables.dao.ProductTypeDao;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.DataGridView;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.myforum.tables.ProductType;

public class CTProductTypePage extends CodeTableBasePage{
	private static final long serialVersionUID = 1L;
	
	public CTProductTypePage(PageParameters params) {
		List<ICellPopulator<ProductType>> columns = new ArrayList<ICellPopulator<ProductType>>();

        columns.add(new CTPopulator<ProductType>("code"));
        columns.add(new CTPopulator<ProductType>("description"));
        
        add(new Label("codetable", Model.of("Product Types")));
        
        add(new DataGridView<ProductType>("rows", columns, new CTProvider()));
	}

	
	private class CTPopulator<T> extends CodeTablePopulator<T>{
		private static final long serialVersionUID = 1L;

		public CTPopulator(String property) {
			super(property);
		}

		@Override
		public String getDescription(IModel<T> rowModel) {
			ProductType productType = (ProductType) rowModel.getObject();
			String description = "";
			switch(property){
				case "code": 		description = String.valueOf(productType.getCode()); break;  
				case "description": description = productType.getDescription();  break;
				default: 			description = "Unknown column: " + property; break;
			}
			return description;
		}

		@Override
		public void rowClick(IModel<T> rowModel) {
			ProductType ProductType = (ProductType) rowModel.getObject();
			int code = ProductType.getCode();
			
			PageParameters params = new PageParameters();
			params.add("code", code);
			
			setResponsePage(ModifyProductTypePage.class, params);
			return;
		}
	}
	
	private class CTProvider extends CodeTableProvider<ProductType> {
		private static final long serialVersionUID = 1L;
		
		public CTProvider() {
			list = new ProductTypeDao().list();
	    }
	        
		@Override
		public IModel<ProductType> model(ProductType object) {
			return Model.of(object);
		}

		@Override
		public int compareRow(ProductType productType1, ProductType productType2) {
			return productType1.getDescription().compareTo(productType2.getDescription());
		}
	}
	
}
