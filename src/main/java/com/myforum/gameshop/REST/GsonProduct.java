package com.myforum.gameshop.REST;

public class GsonProduct {
	private int code;
	private String name;
	private String description;
	private String gameConsole;
	private String productType;
	private String company;
	private byte[] productImage;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getGameConsole() {
		return gameConsole;
	}
	public void setGameConsole(String gameConsole) {
		this.gameConsole = gameConsole;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public byte[] getProductImage() {
		return productImage;
	}
	public void setProductImage(byte[] productImage) {
		this.productImage = productImage;
	}

	
}
