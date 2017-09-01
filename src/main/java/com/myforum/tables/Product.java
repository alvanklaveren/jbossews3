package com.myforum.tables;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.myforum.application.StringLogics;

@Entity
@Table(name = "PRODUCT")
public class Product extends AVKTable implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "code", unique = true, nullable = false)
	private int code;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "code_game_console", nullable=false, insertable=true, updatable=true)
	private GameConsole gameConsole;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "code_product_type", nullable=false, insertable=true, updatable=true)
	private ProductType productType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "code_company", nullable=false, insertable=true, updatable=true)
	private Company company;

	@OneToMany(fetch = FetchType.EAGER, mappedBy="product")
	private Set<ProductImage> productImages = new HashSet<ProductImage>(0);

	@OneToMany(fetch = FetchType.EAGER, mappedBy="product")
	private Set<ProductRating> productRatings = new HashSet<ProductRating>(0);

	public void setCode(int code)
	{
	    this.code = code;
	}
	public int getCode()
	{
	    return code;
	}
	 
	public void setDescription(String description)
	{
	    this.description = description;
	}

	public String getDescription()
	{
	    return description;
	}
	
	/*
	 *  The description does not (and should not) contain HTML codes, e.g. <B>. Therefore, for displaying 
	 *  it in a webpage, we need to translate specific char sequences (like ***) to a HTML code
	 */
	public String getHtmlDescription()
	{
		return StringLogics.prepareMessage( getDescription() );
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public void setGameConsole(GameConsole gameConsole) {
		this.gameConsole = gameConsole;
	}
	
	public GameConsole getGameConsole() {
		return gameConsole;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}
	
	public ProductType getProductType() {
		return productType;
	}

	public Company getCompany() {
		return company;
	}
	
	public void setCompany(Company company) {
		this.company = company;
	}

	public Set<ProductImage> getProductImages() {
		return productImages;
	}
	
	public void setProductImages(Set<ProductImage> productImages) {
		this.productImages = productImages;
	}

	public ProductImage getProductImage() {
		if( productImages.isEmpty() ){
			return null;
		}
		return productImages.iterator().next();
	}

	public void setProductImage(ProductImage productImage) {
		Set<ProductImage> newProductImageSet = new HashSet<ProductImage>();
		newProductImageSet.add(productImage);
		setProductImages(newProductImageSet);	
	}

	public Set<ProductRating> getProductRatings() {
		return productRatings;
	}
	
	public void setProductRatings(Set<ProductRating> productRatings) {
		this.productRatings = productRatings;
	}
	
	public String toString(){
		return getDescription();
	}
}
