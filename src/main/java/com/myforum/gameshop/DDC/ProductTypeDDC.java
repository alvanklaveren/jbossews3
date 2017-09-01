package com.myforum.gameshop.DDC;

import java.util.List;

import com.myforum.framework.AbstractDropDownChoice;
import com.myforum.tables.AVKTable;
import com.myforum.tables.Product;
import com.myforum.tables.ProductType;
import com.myforum.tables.dao.ProductTypeDao;

public class ProductTypeDDC extends AbstractDropDownChoice{
	private static final long serialVersionUID = 1L;

	protected Product product;
	
	public ProductTypeDDC(String wicketId, Product table, String tableColumnName, boolean isAutoSave) {
		super(wicketId, table, tableColumnName, isAutoSave);
		product = (Product) table;
	}

	protected List<ProductType> getList(){
		return (List<ProductType>) new ProductTypeDao().list();
	}	
	
	protected boolean update(AVKTable newSelection) {
		product.setProductType((ProductType) newSelection);
		return true;
	}
}
