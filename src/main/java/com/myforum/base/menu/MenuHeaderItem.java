package com.myforum.base.menu;

public class MenuHeaderItem extends MenuItem {

	public MenuHeaderItem(String itemText) {
		super(EMenuItem.Header);
		setItemText(itemText);
	}
	
	@Override
	public String toHtml(){
		StringBuilder sb = new StringBuilder();
		sb.append("<li class=\"dropdown-header\">");		
		sb.append(getItemText());
		sb.append("</li>");
		return sb.toString();
	}

}
