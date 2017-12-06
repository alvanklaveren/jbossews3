package com.myforum.base.menu;

public class DDMenuHeaderItem extends DDMenuItem {

	public DDMenuHeaderItem(String itemText) {
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
