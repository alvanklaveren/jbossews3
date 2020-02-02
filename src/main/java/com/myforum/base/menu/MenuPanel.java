package com.myforum.base.menu;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myforum.application.CookieLogics;
import com.myforum.security.CredentialLogics;
import com.myforum.tables.ForumUser;
import com.myforum.tables.dao.LoginCredDao;

public class MenuPanel extends Panel {
	private static final long serialVersionUID = 1L;
	private StringBuilder menuItems;

   	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(MenuPanel.class);

	public MenuPanel(String id, int activeMenuItem) {
		super(id);
		
		//Label logoText = new Label("logotext", "ALVANKLAVEREN.COM");
		//add(logoText);
		
		menuItems = new StringBuilder();
		menuItems.append(new MenuItem(EMenuItem.Home, activeMenuItem).toHtml());

		menuItems.append(new MenuItem(EMenuItem.Forum, activeMenuItem).toHtml());
		menuItems.append(new MenuItem(EMenuItem.GameShop, activeMenuItem).toHtml());
		
		//DropDownMenuItem dropDownMenu = new DropDownMenuItem(EMenuItem.Applications.defaultText());
		//dropDownMenu.addMenuItem(new DDMenuItem(EMenuItem.Forum, activeMenuItem));
		//dropDownMenu.addMenuItem(new DDMenuItem(EMenuItem.GameShop, activeMenuItem));
		//dropDownMenu.addMenuItem(new DDMenuItem(EMenuItem.Sources, activeMenuItem));
		//dropDownMenu.addMenuItem(new MenuDividerItem());
		//dropDownMenu.addMenuItem(new DDMenuHeaderItem("More to come"));
		//dropDownMenu.addMenuItem(new MenuItem(EMenuItem.AVKOS).disable());
		//dropDownMenu.addMenuItem(new MenuItem(EMenuItem.AppCreator).disable());
		//menuItems.append(dropDownMenu.toHtml());

		if ( isAdministrator(getActiveUser()) ){
			DropDownMenuItem dropDownMenu2 = new DropDownMenuItem(EMenuItem.CodeTables.defaultText());
			dropDownMenu2.addMenuItem(new DDMenuItem(EMenuItem.CTCompany, activeMenuItem));
			dropDownMenu2.addMenuItem(new DDMenuItem(EMenuItem.CTGameConsole, activeMenuItem));
			dropDownMenu2.addMenuItem(new DDMenuItem(EMenuItem.CTProductType, activeMenuItem));
			menuItems.append(dropDownMenu2.toHtml());
		}
		
		menuItems.append(new MenuItem(EMenuItem.Articles, activeMenuItem).toHtml());
		menuItems.append(new MenuItem(EMenuItem.AboutMe, activeMenuItem).toHtml());

		
		Label menuItemLabel = new Label("menuItems", new Model<String>(menuItems.toString()) );
		menuItemLabel.setEscapeModelStrings(false);
		add(menuItemLabel);

//		REMOVED LOGO. Replaced by plain text (with alternating colors)
		//		// I have no idea why, but sometimes the weblogo HTML cannot be found and returns an error.
//		// So this piece of code is to debug this problem, or at least prevent the website from returning to the homepage.
//		try{
//			add( ForumUtils.getWebLogo() );
//		}catch(Exception e){
//			log.error(e.getMessage());
//		}
	}

	public ForumUser getActiveUser() {
		String magicToken = CookieLogics.getCookie("magictoken");
		if(new LoginCredDao().tokenExpired(magicToken)){
			return null;
		}
		
		ForumUser activeUser = new LoginCredDao().getForumUserByMagicToken(magicToken);
		return activeUser;
	}

	protected boolean isAdministrator(ForumUser forumUser){
		return CredentialLogics.isAdministrator(forumUser);
	}
	
	@Override 
	protected void onBeforeRender() { 
	 super.onBeforeRender(); 
	} 
	 
	@Override public void renderHead(IHeaderResponse response){
	}
    
}
