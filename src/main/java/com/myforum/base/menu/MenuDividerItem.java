package com.myforum.base.menu;

public class MenuDividerItem extends MenuItem {

	public MenuDividerItem() {
		super(EMenuItem.Divider);
	}

	@Override
	public String toHtml(){
		return "<li role=\"separator\" class=\"divider\"></li>";
	}
}
