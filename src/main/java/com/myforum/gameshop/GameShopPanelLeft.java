package com.myforum.gameshop;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import com.myforum.base.BasePanelLeft;
import com.myforum.dictionary.EText;
import com.myforum.dictionary.Translator;
import com.myforum.framework.AVKLabel;
import com.myforum.tables.GameConsole;
import com.myforum.tables.ProductType;
import com.myforum.tables.dao.GameConsoleDao;
import com.myforum.tables.dao.ProductTypeDao;

public class GameShopPanelLeft extends BasePanelLeft {

	private static final long serialVersionUID = 1L;

	private Translator translator = Translator.getInstance();
	
	public GameShopPanelLeft(String id) {
		super(id);

		Label navigationLabel = new AVKLabel("navigation", "Navigation");
		add(navigationLabel);

		StringBuilder sb = new StringBuilder();
		
		List<GameConsole> gameConsoleList = new GameConsoleDao().list();
		List<ProductType> productTypeList = new ProductTypeDao().list(); 
		
		sb.append("<div class=\"sidenav\">");
		sb.append("<a href=\"/gameshop\" class=\"dropdown-btn\">"+ translator.translate( EText.RECENTLY_ADDED ) +"</a>");
	    sb.append("<br style=\"line-height: 1px;\"/>");

		for(GameConsole gameConsole:gameConsoleList){
			sb.append("<a href=\"#\" class=\"dropdown-btn\">" + gameConsole.getDescription() + "</a>");

			sb.append("<div class=\"dropdown-container\">");
			for(ProductType productType:productTypeList){
			    sb.append("<a href=\"/gameshop/" + gameConsole.getCode() + "/" + productType.getCode() + "\">" + productType.getDescription() + "</a>");
			}
		    sb.append("</div>");
		    sb.append("<br style=\"line-height: 1px;\"/>");
		}
	    sb.append("</div>");

		Label leftSideMenuLabel = new Label("leftsidemenu", new Model<String>( sb.toString() ));
		leftSideMenuLabel.setEscapeModelStrings(false);
		add(leftSideMenuLabel);
		
	}
}
