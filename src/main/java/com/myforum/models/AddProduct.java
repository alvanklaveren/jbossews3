package com.myforum.models;

import java.io.Serializable;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.myforum.application.StringLogics;
import com.myforum.tables.Product;

public class AddProduct extends Product implements Serializable{
	private static final long serialVersionUID = 1L;

	public String getEditDescription(){ return getDescription(); }
	public String getEditName(){ return getName(); }
	public int	  getEditYear() { return getYear(); }

	public void setEditDescription(String description){ setDescription(description); }
	public void setEditName(String name){ setName(name); }
	public void setEditYear(int year) { setYear(year); }
	
	/*
	 * Turns the necessary fields of products used in this form to page parameters
	 */
	public PageParameters toPageParameters(){
		PageParameters params = new PageParameters();
		if(!StringLogics.isEmpty(getName())){ 		params.add("name", 		   getName()); }
		if(!StringLogics.isEmpty(getDescription())){ params.add("description", getDescription()); }
		params.add("year", getEditYear());
		
		if(getGameConsole() != null){ params.add("console", getGameConsole().getCode()); }
		if(getProductType() != null){ params.add("type",    getProductType().getCode()); }
		if(getCompany()	    != null){ params.add("company", getCompany().getCode()); }
		return params;
	}

	/*
	 * Convert to Product (so Hibernate can save it)
	 */
	
	public Product toProduct(){
		Product product = new Product();
		product.setCode(getCode());
		product.setName(getName());
		product.setYear(getYear());
		product.setDescription(getDescription());
		product.setGameConsole(getGameConsole());
		product.setProductType(getProductType());
		product.setCompany(getCompany());
		product.setProductImages(getProductImages());
		
		return product;
	}

}
