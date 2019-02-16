package com.myforum.models;

import java.io.Serializable;

import org.apache.wicket.model.CompoundPropertyModel;

public class AddProductModel extends CompoundPropertyModel<AddProduct> implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public AddProductModel(AddProduct product) {
		super(product);
	}
	
	public String getEditDescription(){ return getObject().getDescription(); }
	
	public void setEditDescription(String description){ getObject().setDescription(description); }

	public String getEditName(){ return getObject().getName(); }

	public void setEditName(String name){ getObject().setName(name); }
	
	public int getYear() { return getObject().getYear(); }
	
	public void setYear(int year) { getObject().setYear(year); }

}
