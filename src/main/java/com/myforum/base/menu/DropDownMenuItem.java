package com.myforum.base.menu;

import java.util.ArrayList;

public class DropDownMenuItem {
	private String 				itemText;
	private ArrayList<MenuItem>	menuItems;
	
	public DropDownMenuItem(String itemText){
		this.itemText = itemText;
		menuItems = new ArrayList<MenuItem>();
	}

	public void addMenuItem(MenuItem menuItem){
		menuItems.add(menuItem);
	}
	
	public String toHtml(){
		return createDynamicHtml(itemText);
	};
	
	private String createDynamicHtml(String displayText) {
	    StringBuilder sb = new StringBuilder();
	    sb.append("<li class=\"dropdown\">");
	    sb.append("<a href=\"#\" class=\"dropdown-toggle\" ");
	    sb.append("data-toggle=\"dropdown\" role=\"button\" ");
	    sb.append("aria-haspopup=\"true\" aria-expanded=\"false\"");
	    sb.append(">");
	    sb.append(itemText);
	    sb.append("<span class=\"caret\"></span></a>");

	    sb.append("<ul class=\"dropdown-menu\">");
	    for(MenuItem menuItem:menuItems){
	    	sb.append(menuItem.toHtml());
	    }
	    sb.append("</ul>");
	    sb.append("</li>");

	    return sb.toString(); 
	}

}
