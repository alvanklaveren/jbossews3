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

		sb.append("<div class=\"sidemenu-item\">");
		sb.append("<h4><a href=\"/gameshop\">"+ translator.translate( EText.RECENTLY_ADDED ) +"</a></h4>");
	    sb.append("</div>");
   
		for(GameConsole gameConsole:gameConsoleList){
			sb.append("<div class=\"sidemenu-item\">");
			sb.append("<h4><a href=\"/gameshop/" + gameConsole.getCode() + "\">" + gameConsole.getDescription() + "</a></h4>");
		    sb.append("<ul class=\"sidemenu-item\">");
			for(ProductType productType:productTypeList){
			    sb.append("<li class=\"sidemenu-item\"><a class=\"sidemenu-item\" href=\"/gameshop/" + gameConsole.getCode() + "/" + productType.getCode() + "\">" + productType.getDescription() + "</a></li>");
			}
		    sb.append("</ul>");
		    sb.append("</div>");
		}
		
		Label leftSideMenuLabel = new Label("leftsidemenu", new Model<String>( sb.toString() ));
		leftSideMenuLabel.setEscapeModelStrings(false);
		add(leftSideMenuLabel);
		
	}
}
