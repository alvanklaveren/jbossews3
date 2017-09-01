package com.myforum.base.menu;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myforum.application.ForumUtils;

public class MenuPanel extends Panel {
	private static final long serialVersionUID = 1L;
	private StringBuilder menuItems;

   	private Logger log = LoggerFactory.getLogger(MenuPanel.class);

	public MenuPanel(String id, int activeMenuItem) {
		super(id);
		menuItems = new StringBuilder();
		menuItems.append(new MenuItem(EMenuItem.Home, activeMenuItem).toHtml());
		menuItems.append(new MenuItem(EMenuItem.AboutMe, activeMenuItem).toHtml());
		menuItems.append(new MenuItem(EMenuItem.Articles, activeMenuItem).toHtml());
		
		DropDownMenuItem dropDownMenu = new DropDownMenuItem(EMenuItem.Applications.defaultText());
		
		dropDownMenu.addMenuItem(new MenuItem(EMenuItem.Forum, activeMenuItem));
		//dropDownMenu.addMenuItem(new MenuItem(EMenuItem.Sources, activeMenuItem));
		dropDownMenu.addMenuItem(new MenuItem(EMenuItem.GameShop, activeMenuItem));
		dropDownMenu.addMenuItem(new MenuDividerItem());
		dropDownMenu.addMenuItem(new MenuHeaderItem("More to come"));
		//dropDownMenu.addMenuItem(new MenuItem(EMenuItem.AVKOS).disable());
		//dropDownMenu.addMenuItem(new MenuItem(EMenuItem.AppCreator).disable());

		menuItems.append(dropDownMenu.toHtml());
		
		Label menuItemLabel = new Label("menuItems", new Model<String>(menuItems.toString()) );
		menuItemLabel.setEscapeModelStrings(false);
		add(menuItemLabel);

		// I have no idea why, but sometimes the weblogo HTML cannot be found and returns an error.
		// So this piece of code is to debug this problem, or at least prevent the website from returning to the homepage.
		try{
			add( ForumUtils.getWebLogo() );
		}catch(Exception e){
			log.error(e.getMessage());
		}
	}

	 @Override 
	 protected void onBeforeRender() { 
	  super.onBeforeRender(); 
	 } 
	 
	@Override public void renderHead(IHeaderResponse response){
	}
    
}
