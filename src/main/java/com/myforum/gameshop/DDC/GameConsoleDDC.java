package com.myforum.gameshop.DDC;

import java.util.List;

import com.myforum.framework.AbstractDropDownChoice;
import com.myforum.tables.AVKTable;
import com.myforum.tables.Product;
import com.myforum.tables.GameConsole;
import com.myforum.tables.dao.GameConsoleDao;

public class GameConsoleDDC extends AbstractDropDownChoice{
	private static final long serialVersionUID = 1L;

	protected Product product;
	
	public GameConsoleDDC(String wicketId, Product table, String tableColumnName, boolean isAutoSave) {
		super(wicketId, table, tableColumnName, isAutoSave);
		product = (Product) table;
	}

	protected List<GameConsole> getList(){
		return (List<GameConsole>) new GameConsoleDao().list();
	}	
	
	protected boolean update(AVKTable newSelection) {
		product.setGameConsole((GameConsole) newSelection);
		return true;
	}
}
