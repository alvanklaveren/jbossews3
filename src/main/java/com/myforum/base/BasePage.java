package com.myforum.base;

import java.io.Serializable;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.protocol.http.ClientProperties;
import org.apache.wicket.protocol.http.request.WebClientInfo;
//import org.apache.wicket.protocol.https.RequireHttps;
import org.apache.wicket.settings.IRequestCycleSettings;

import com.myforum.application.CookieLogics;
import com.myforum.application.DBHelper;
import com.myforum.application.ForumUtils;
import com.myforum.base.dictionary.ELanguage;
import com.myforum.base.dictionary.EText;
import com.myforum.base.dictionary.Translator;
import com.myforum.base.menu.EMenuItem;
import com.myforum.base.menu.MenuPanel;
import com.myforum.framework.ErrorLabel;
import com.myforum.framework.LanguageLabel;
import com.myforum.homepage.HomePage;
import com.myforum.security.CredentialLogics;
import com.myforum.tables.ForumUser;
import com.myforum.tables.dao.LoginCredDao;

/*disabled HTTPS... is now a paid feature in openshift!*/
/*@RequireHttps*/ 
public class BasePage extends WebPage implements Serializable{
	private static final long 	serialVersionUID = 1L;

	private String		 modalPassValue;
	
	protected String 	 magicToken;
	protected Panel 	 panelLeft, panelRight;
	protected Translator translator = Translator.getInstance();
	
	public BasePage(){
		initPage(EMenuItem.DUMMY.id()); // no active page
	}
 
	public BasePage(EMenuItem menuItem){
		initPage(menuItem.id()); // no active page
	    ForumUtils.debugInfo(this);	
	}
	
	private void initPage(int activeMenuItem){
		setVersioned(false);
		setStatelessHint(true);
		DBHelper.closeSession();

		if ( !CredentialLogics.canOpen(getActiveUser(), this) ){
			setResponsePage(HomePage.class);
			return;
		}
		
		if(CookieLogics.getCookie("agreedToCookie").equals("yes")){
			add(new Label("agreeToCookies", ""));
		}else{
			Panel agreeToCookies = new AgreeToCookiesPanel(this, "agreeToCookies");
			addOrReplace(agreeToCookies);		
		}

		add( new LanguageLabel("english", "EN", ELanguage.English) );
		add( new LanguageLabel("dutch",   "NL", ELanguage.Dutch) );

		add( new Label("pagetitle", getPageTitle()) );
		
		Panel topMenu = new MenuPanel("topMenu", activeMenuItem);
		add(topMenu);

		panelLeft = new BasePanelLeft("sidepanelleft");
		add(panelLeft);

		panelRight = new BasePanelRight("sidepanelright");
		add(panelRight);
		
		// do not show the right panel, when on mobile devices
		// this unfortunately DOES trigger some javascript, which may result in info shown on browser before continuing to page
		// this has been replaced by java code IN the basepage.html
		//if( getClientProperties().getBrowserWidth() <= 800) {
		//	panelRight.setVisible(false);
		//}

		// Not all pages will show error messages, but the ones that do (and should) have different "ideal" locations to show this error,
		// so we allow putting it in each page manually, but we do not want to explicitly repeat the code for it, hence the below try-catch
		try {
				addOrReplace( new ErrorLabel() );
		}catch(Exception e) {
			// do nothing... it is okay if it did not succeed
		}
		
		Panel footer = new FooterPanel("footer");
		add(footer);
		
	}

	public void setErrorMessage( String errorMessage ){
		getSession().setAttribute( "errormessage", translator.translate( errorMessage ) );
	}

	public void setErrorMessage( EText eText ){
		getSession().setAttribute( "errormessage", translator.translate( eText.toString() ) );
	}

	public void resetErrorMessage(){
		String nullString = null;
		setErrorMessage( nullString );
	}

	protected boolean isAdministrator(ForumUser forumUser){
		return CredentialLogics.isAdministrator(forumUser);
	}
	
	protected boolean allowModification(ForumUser forumUser){
		return isAdministrator(getActiveUser()) || isActive(forumUser);
	}

	public boolean isActive(ForumUser forumUser){
		if( getActiveUser() == null ){ return false; }
		if( getActiveUser().getCode() <= 0 ){ return false; }

		if( forumUser == null ){ return false; }
		if( forumUser.getCode() <= 0 ){ return false; }
		
		if( getActiveUser().getCode() != forumUser.getCode() ) { return false; };
		return true;
	}

	
	public ForumUser getActiveUser() {
		magicToken = CookieLogics.getCookie("magictoken");
		if(new LoginCredDao().tokenExpired(magicToken)){
			return null;
		}
		
		ForumUser activeUser = new LoginCredDao().getForumUserByMagicToken(magicToken);
		return activeUser;
	}

	public String getModalPassValue() {
        return modalPassValue;
    }

    public void setModalPassValue(String passValue) {
        this.modalPassValue = passValue;
    }
	
    protected String getPageTitle() {
    	return "AVK - Homepage";
    }
    
	 /**
     * A helper function that makes sure that gathering of extended browser info
     * is enabled when reading the ClientInfo's properties
     *
     * @return the currently available client info
     */
    protected ClientProperties getClientProperties()
    {
        IRequestCycleSettings requestCycleSettings = getApplication().getRequestCycleSettings();
        boolean gatherExtendedBrowserInfo = requestCycleSettings.getGatherExtendedBrowserInfo();
        ClientProperties properties = null;
        try
        {
            requestCycleSettings.setGatherExtendedBrowserInfo(true);
            WebClientInfo clientInfo = (WebClientInfo) getSession().getClientInfo();
            properties = clientInfo.getProperties();
        }
        finally
        {
            requestCycleSettings.setGatherExtendedBrowserInfo(gatherExtendedBrowserInfo);
        }
        return properties;
    }
    
	@Override public void renderHead(IHeaderResponse response){
//		  response.render(CssHeaderItem.forReference(new CssResourceReference(BasePage.class,"css/basepage.css")));
//		  response.render(JavaScriptHeaderItem.forReference(new JQueryPluginResourceReference(HomePage.class,"HomePage.js")));
//		  response.render(new FilteredHeaderItem(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(HomePage.class,"top.js")),JavaScriptFilteredIntoFooterHeaderResponse.HEADER_FILTER_NAME));
		}

	@Override
	protected void onAfterRender()
	{
		// make sure to close hibernate session to prevent java heap space issues in Wildfly
		DBHelper.closeSession();
		super.onAfterRender();
	}
}
