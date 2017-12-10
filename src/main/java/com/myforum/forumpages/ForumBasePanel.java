package com.myforum.forumpages;

import org.apache.wicket.markup.html.panel.Panel;

import com.myforum.application.CookieLogics;
import com.myforum.application.DBHelper;
import com.myforum.base.dictionary.Translator;
import com.myforum.forumpages.header.HeaderPanel;
import com.myforum.homepage.HomePage;
import com.myforum.security.CredentialLogics;
import com.myforum.tables.ForumUser;
import com.myforum.tables.dao.LoginCredDao;

public abstract class ForumBasePanel extends Panel{
	private static final long serialVersionUID = 1L;
	protected String magicToken;
	protected ForumBasePage parent;
	protected Translator translator = Translator.getInstance();

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
	
	public void setErrorMessage( String errorMessage ){
		getSession().setAttribute( "errormessage", Translator.getInstance().translate(errorMessage) );
	}
	
	public void resetErrorMessage(){
		setErrorMessage( null );
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
