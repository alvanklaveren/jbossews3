package com.myforum.base.menu;

public class MenuDividerItem extends DDMenuItem {

	public MenuDividerItem() {
		super(EMenuItem.Divider);
	}

	@Override
	public String toHtml(){
		return "<div class=\"dropdown-divider\"></div>";
	}
}
