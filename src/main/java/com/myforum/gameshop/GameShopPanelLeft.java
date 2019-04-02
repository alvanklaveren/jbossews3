package com.myforum.gameshop;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import com.myforum.base.BasePanelLeft;
import com.myforum.base.dictionary.EText;
import com.myforum.base.dictionary.Translator;
import com.myforum.tables.GameConsole;
import com.myforum.tables.ProductType;
import com.myforum.tables.dao.GameConsoleDao;
import com.myforum.tables.dao.ProductTypeDao;

public class GameShopPanelLeft extends BasePanelLeft {

	private static final long serialVersionUID = 1L;

	private Translator translator = Translator.getInstance();
	
	public GameShopPanelLeft(String id) {
		super(id);

		StringBuilder sb = new StringBuilder();
		
		List<GameConsole> gameConsoleList = new GameConsoleDao().list();
		List<ProductType> productTypeList = new ProductTypeDao().list(); 

	    sb.append("<br/>");
		sb.append("<div class=\"sidenav\">");
		sb.append("<a href=\"/gameshop\">"+ translator.translate( EText.RECENTLY_ADDED ) +"</a>");
	    sb.append("<br/>");

		for(GameConsole gameConsole:gameConsoleList){
			sb.append("<button class=\"dropdown-btn\">" + gameConsole.getDescription() + "</button>");

			sb.append("<div class=\"dropdown-container\">");
			for(ProductType productType:productTypeList){
			    sb.append("<a href=\"/gameshop/" + gameConsole.getCode() + "/" + productType.getCode() + "\">" + productType.getDescription() + "</a>");
			}
		    sb.append("</div>");
		    sb.append("<br/>");
		}

	    sb.append("</div>");

		Label leftSideMenuLabel = new Label("leftsidemenu", new Model<String>( sb.toString() ));
		leftSideMenuLabel.setEscapeModelStrings(false);
		add(leftSideMenuLabel);
		
	}
}
