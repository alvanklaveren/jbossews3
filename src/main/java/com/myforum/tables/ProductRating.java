package com.myforum.tables;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PRODUCT_RATING")
public class ProductRating extends AVKTable implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "code", unique = true, nullable = false)
	private int code;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "code_product", nullable=false, insertable=true, updatable=true)
	private Product product;

	@Column(name = "rating", nullable=false, insertable=true, updatable=true)
	private int rating;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "code_rating_url", nullable=false, insertable=true, updatable=true)
	private RatingUrl ratingUrl;
	
	
	public void setCode(int code)
	{
	    this.code = code;
	}

	public int getCode(){
	    return code;
	}

	public void setRating(int rating){
	    this.rating = rating;
	}

	public int getRating(){
	    return rating;
	}
	
	public void setProduct(Product product) {
		this.product = product;
	}
	
	public Product getProduct(){
		return product;
	}
	
	public RatingUrl getRatingUrl(){
		return ratingUrl;
	}
	
	public void setRatingUrl(RatingUrl ratingUrl) {
		this.ratingUrl = ratingUrl;
	}


}
