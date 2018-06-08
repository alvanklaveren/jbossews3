package com.myforum.gameshop.codetable;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.myforum.tables.dao.GameConsoleDao;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.DataGridView;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.myforum.tables.GameConsole;

public class CTGameConsolePage extends CodeTableBasePage{
	private static final long serialVersionUID = 1L;
	
	public CTGameConsolePage(PageParameters params) {
		List<ICellPopulator<GameConsole>> columns = new ArrayList<ICellPopulator<GameConsole>>();

        columns.add(new CTPopulator<GameConsole>("code"));
        columns.add(new CTPopulator<GameConsole>("description"));
        columns.add(new CTPopulator<GameConsole>("sortorder"));
        
        add(new Label("codetable", Model.of("Game Consoles")));
        
        add(new DataGridView<GameConsole>("rows", columns, new CTProvider()));
	}

	
	private class CTPopulator<T> extends CodeTablePopulator<T>{
		private static final long serialVersionUID = 1L;

		public CTPopulator(String property) {
			super(property);
		}

		@Override
		public String getDescription(IModel<T> rowModel) {
			GameConsole gameConsole = (GameConsole) rowModel.getObject();
			String description = "";
			switch(property){
				case "code": 		description = String.valueOf(gameConsole.getCode()); break;  
				case "description": description = gameConsole.getDescription();  break;
				case "sortorder":   description = String.valueOf(gameConsole.getSortorder());  break;
				default: 			description = "Unknown column: " + property; break;
			}
			return description;
		}

		@Override
		public void rowClick(IModel<T> rowModel) {
			GameConsole GameConsole = (GameConsole) rowModel.getObject();
			int code = GameConsole.getCode();
			
			PageParameters params = new PageParameters();
			params.add("code", code);
			
			setResponsePage(ModifyGameConsolePage.class, params);
			return;
		}
	}
	
	private class CTProvider extends CodeTableProvider<GameConsole> {
		private static final long serialVersionUID = 1L;
		
		public CTProvider() {
			list = new GameConsoleDao().list();
	    }
	        
		@Override
		public IModel<GameConsole> model(GameConsole object) {
			return Model.of(object);
		}

		@Override
		public int compareRow(GameConsole gameConsole1, GameConsole gameConsole2) {
			return gameConsole1.getDescription().compareTo(gameConsole2.getDescription());
		}
	}
	
}
