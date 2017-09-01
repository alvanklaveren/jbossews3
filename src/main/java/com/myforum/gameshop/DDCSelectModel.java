package com.myforum.gameshop;

import java.io.Serializable;

import com.myforum.tables.GameConsole;
import com.myforum.tables.ProductType;
import com.myforum.tables.dao.GameConsoleDao;
import com.myforum.tables.dao.ProductTypeDao;

public class DDCSelectModel implements Serializable{
	private static final long serialVersionUID = 1L;

	int gameConsoleId = 0;
	int productTypeId = 0;

	/*
	 *  A model used by the dropdownlists at the top of the webpage where you can select console and game.
	 *  It defaults the text when nothing is selected and is used to instruct retrieval based on a change in the dropdowns
	 */
	public DDCSelectModel(){}
	
	public String getGameConsoleDescription(){
		GameConsole gameConsole = new GameConsoleDao().find(gameConsoleId);
		String description = "- Please Select -";
		if(gameConsole != null){
			description = gameConsole.getDescription();
		}
		return description;
	}

	public String getProductTypeDescription(){
		ProductType productType = new ProductTypeDao().find(productTypeId);
		String description = "- Please Select -";
		if(productType != null){
			description = productType.getDescription();
		}
		return description;
	}

	public int getGameConsoleId() {
		return gameConsoleId;
	}
	public void setGameConsoleId(int gameConsoleId) {
		this.gameConsoleId = gameConsoleId;
	}
	public int getProductTypeId() {
		return productTypeId;
	}
	public void setProductTypeId(int productTypeId) {
		this.productTypeId = productTypeId;
	}
}
