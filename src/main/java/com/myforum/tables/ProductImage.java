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
@Table(name = "PRODUCT_IMAGE")
public class ProductImage extends AVKTable implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue
	@Column(name = "code", unique = true, nullable = false)
	private int code;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "code_product", nullable=false, insertable=true, updatable=true)
	private Product product;

	@Column(name = "image")
	private byte[] image;

	@Column(name = "sortorder")
	private int sortorder;
	
	public void setCode(int code) {
		this.code = code;
	}
	public int getCode() {
		return code;
	}

	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}

	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public int getSortorder() {
		return sortorder;
	}
	public void setSortorder(int sortorder) {
		this.sortorder = sortorder;
	}

	public String toString(){
		return "image";
	}

}
