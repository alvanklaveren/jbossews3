package com.myforum.base.menu;

import java.util.ArrayList;

public class DropDownMenuItem {
	private String 				itemText;
	private ArrayList<DDMenuItem>	menuItems;
	
	public DropDownMenuItem(String itemText){
		this.itemText = itemText;
		menuItems = new ArrayList<DDMenuItem>();
	}

	public void addMenuItem(DDMenuItem ddMenuItem){
		menuItems.add(ddMenuItem);
	}
	
	public String toHtml(){
		return createDynamicHtml(itemText);
	};
	
	private String createDynamicHtml(String displayText) {
	    StringBuilder sb = new StringBuilder();
	    sb.append("<li class=\"nav-item dropdown\">");
	    sb.append("<a href=\"#\" class=\"nav-link dropdown-toggle\" ");
	    sb.append("data-toggle=\"dropdown\" role=\"button\" id=\"navbarDropdown\" ");
	    sb.append("aria-haspopup=\"true\" aria-expanded=\"false\"");
	    sb.append(">");
	    sb.append(itemText);
	    sb.append("</a>");

	    sb.append("<div class=\"dropdown-menu\" aria-labelledby=\"navbarDropdown\">");
	    for(DDMenuItem menuItem:menuItems){
	    	sb.append(menuItem.toHtml());
	    }
	    sb.append("</div>");

	    return sb.toString(); 
	}

}
