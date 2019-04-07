package com.myforum.forumpages;

import com.myforum.application.CookieLogics;
import com.myforum.application.DBHelper;
import com.myforum.forumpages.header.HeaderPanel;
import com.myforum.framework.AVKPanel;
import com.myforum.homepage.HomePage;
import com.myforum.security.CredentialLogics;
import com.myforum.tables.ForumUser;
import com.myforum.tables.dao.LoginCredDao;

public abstract class ForumBasePanel extends AVKPanel{
	private static final long serialVersionUID = 1L;
	protected String magicToken;
	protected ForumBasePage parent;

	public ForumBasePanel(final ForumBasePage parent) {
		super("activepanel");
		setOutputMarkupId(true);
		parent.addOrReplace(new HeaderPanel());
		this.parent = parent;
		DBHelper.closeSession();
		
		if ( !CredentialLogics.canOpen(getActiveUser(), this) ){
			setResponsePage(HomePage.class);
			return;
		};
	}
	
	protected boolean isAdministrator(ForumUser forumUser){
		return CredentialLogics.isAdministrator(forumUser);
	}
	
	protected boolean allowModification(ForumUser forumUser){
		return isAdministrator(forumUser) || isActive(forumUser);
	}

	public boolean isActive(ForumUser forumUser){
		if(getActiveUser() == null || getActiveUser().getCode() != forumUser.getCode()) return false;
		return true;
	}
	
	public ForumUser getActiveUser() {
		magicToken = CookieLogics.getCookie("magictoken");
		ForumUser activeUser =  new LoginCredDao().getForumUserByMagicToken(magicToken);
		return activeUser;
	}
	}
