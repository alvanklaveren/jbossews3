package com.myforum.tables;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "PRODUCT_TYPE")
public class ProductType extends AVKTable implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue
	@Column(name = "code", unique = true, nullable = false)
	private int code;

	@Column(name = "description")
	private String description;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="productType")
	private Set<Product> products = new HashSet<Product>(0);
	
	public void setCode(int code) {
		this.code = code;
	}
	public int getCode() {
		return code;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}
	
	public void setProducts(Set<Product> products) {
		this.products = products;
	}
	public Set<Product> getProducts() {
		return products;
	}
	
	public String toString(){
		return getDescription();
	}

}
