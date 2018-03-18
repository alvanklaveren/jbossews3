package com.myforum.gameshop.codetable;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.myforum.tables.dao.CompanyDao;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.DataGridView;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.myforum.tables.Company;

public class CTCompanyPage extends CodeTablePage{
	private static final long serialVersionUID = 1L;
	
	public CTCompanyPage(PageParameters params) {
		List<ICellPopulator<Company>> columns = new ArrayList<ICellPopulator<Company>>();

        columns.add(new CTPopulator<Company>("code"));
        columns.add(new CTPopulator<Company>("description"));
        
        add(new Label("codetable", Model.of("Companies")));
        
        add(new DataGridView<Company>("rows", columns, new CTProvider()));
	}

	
	private class CTPopulator<T> extends CodeTablePopulator<T>{
		private static final long serialVersionUID = 1L;

		public CTPopulator(String property) {
			super(property);
		}

		@Override
		public String getDescription(IModel<T> rowModel) {
			Company company = (Company) rowModel.getObject();
			String description = "";
			switch(property){
				case "code": 		description = String.valueOf(company.getCode()); break;  
				case "description": description = company.getDescription();  break;
				default: 			description = "Unknown column: " + property; break;
			}
			return description;
		}

		@Override
		public void rowClick(IModel<T> rowModel) {
			Company company = (Company) rowModel.getObject();
			int code = company.getCode();
			
			PageParameters params = new PageParameters();
			params.add("code", code);
			
			setResponsePage(ModifyCompanyPage.class, params);
			return;
		}
	}
	
	private class CTProvider extends CodeTableProvider<Company> {
		private static final long serialVersionUID = 1L;
		
		public CTProvider() {
			list = new CompanyDao().list();
	    }
	        
		@Override
		public IModel<Company> model(Company object) {
			return Model.of(object);
		}

		@Override
		public int compareRow(Company company1, Company company2) {
			return company1.getDescription().compareTo(company2.getDescription());
		}
	}
	
}
