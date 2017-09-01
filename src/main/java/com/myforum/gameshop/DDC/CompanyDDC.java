package com.myforum.gameshop.DDC;

import java.util.List;

import com.myforum.framework.AbstractDropDownChoice;
import com.myforum.tables.AVKTable;
import com.myforum.tables.Company;
import com.myforum.tables.Product;
import com.myforum.tables.dao.CompanyDao;

public class CompanyDDC extends AbstractDropDownChoice{
	private static final long serialVersionUID = 1L;
	
	Product product;

	public CompanyDDC(String wicketId, Product table, String tableColumnName, boolean isAutoSave) {
		super(wicketId, table, tableColumnName, isAutoSave);
		product = (Product) table;
	}

	protected List<Company> getList(){
		return (List<Company>) new CompanyDao().list();
	}	
	
	@Override
	protected boolean update(AVKTable newSelection) {
		product.setCompany((Company) newSelection);
		return true;
	}
}
