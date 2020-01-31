package com.myforum.base;

import java.io.Serializable;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.ClientProperties;
import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.apache.wicket.settings.IRequestCycleSettings;

import com.myforum.application.CookieLogics;
import com.myforum.application.DBHelper;
import com.myforum.application.ETheme;
import com.myforum.application.ForumUtils;
import com.myforum.base.menu.EMenuItem;
import com.myforum.base.menu.MenuPanel;
import com.myforum.dictionary.ELanguage;
import com.myforum.dictionary.EText;
import com.myforum.dictionary.Translator;
import com.myforum.framework.ErrorLabel;
import com.myforum.framework.LanguageLabel;
import com.myforum.framework.SuccessLabel;
import com.myforum.framework.ThemeButton;
import com.myforum.homepage.HomePage;
import com.myforum.security.CredentialLogics;
import com.myforum.tables.ForumUser;
import com.myforum.tables.dao.LoginCredDao;


public class BasePage extends WebPage implements IRequiresHttps, Serializable{
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

		boolean defaultThemeActive = false;
		if(CookieLogics.getCookieInt(ETheme.cookieName) == 0) {
			CookieLogics.setCookieForever(ETheme.cookieName, ETheme.Light.getId());
			defaultThemeActive = true;
		} else {
			defaultThemeActive = false;
		}		
		
		add( new ThemeButton("light", ETheme.Light.getDescription(), ETheme.Light, defaultThemeActive) );
		add( new ThemeButton("dark",  ETheme.Dark.getDescription(),  ETheme.Dark) );

		add( new LanguageLabel("flag_gb", ELanguage.English) );
		add( new LanguageLabel("flag_nl", ELanguage.Dutch) );
		
		ELanguage selectedLanguage = Translator.getInstance().getDefaultLanguage();
		String labelText  = "<img src=\"" + selectedLanguage.getFontFile() + "\" width=" + selectedLanguage.getFontWidth() + "px height=" + selectedLanguage.getFontHeight() + "px alt=\"" + selectedLanguage.getIsoA2() + "\">";
		Label selectedLanguageLabel = new Label("flag_selected", new Model<String>(labelText));
		selectedLanguageLabel.setEscapeModelStrings(false);
		add(selectedLanguageLabel);	 

		add( new Label("pagetitle", getPageTitle()) );
		
		// Top Menu
		Panel topMenu = new MenuPanel("topMenu", activeMenuItem);
		add(topMenu);

		// Left Side Page
		panelLeft = new BasePanelLeft("sidepanelleft");
		add(panelLeft);

		// Right Side Page
		panelRight = new BasePanelRight("sidepanelright");
		add(panelRight);
		
		// Error Label
		WebMarkupContainer errorDiv = new WebMarkupContainer("errordiv");
		addOrReplace( errorDiv );

		ErrorLabel errorLabel = new ErrorLabel();
		errorDiv.addOrReplace( errorLabel );

		errorDiv.setVisible(errorLabel.isVisible());			

		// Success Label
		WebMarkupContainer successDiv = new WebMarkupContainer("successdiv");
		addOrReplace( successDiv );

		SuccessLabel successLabel = new SuccessLabel();
		successDiv.addOrReplace( successLabel );

		successDiv.setVisible(successLabel.isVisible());			

		
		// Footer
		Panel footer = new FooterPanel("footer");
		add(footer);
		
		getSession().setAttribute( "errormessage", "");	
		getSession().setAttribute( "successmessage", "");	
	}

	public void setErrorMessage( String errorMessage ){
		getSession().setAttribute( "errormessage", translator.translate( errorMessage ) );
	}

	public void setErrorMessage( EText eText ){
		getSession().setAttribute( "errormessage", translator.translate( eText.toString() ) );
	}

	public void setSuccessMessage( String succesMessage ){
		getSession().setAttribute( "successmessage", translator.translate( succesMessage ) );
	}

	public void setSuccessMessage( EText eText ){
		getSession().setAttribute( "successmessage", translator.translate( eText.toString() ) );
	}

	public void setSuccessMessage( EText eText, String succesMessage ){
		getSession().setAttribute( "successmessage", translator.translate( eText.toString() ) + translator.translate( succesMessage ) );
	}

	public void setPanelErrorMessage( String errorMessage ){
		getSession().setAttribute( "panelerrormessage", translator.translate( errorMessage ) );
	}

	public void setPanelErrorMessage( EText eText ){
		getSession().setAttribute( "panelerrormessage", translator.translate( eText.toString() ) );
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
		// response.render(CssHeaderItem.forReference(new CssResourceReference(BasePage.class,"css/basepage.css")));
		// response.render(JavaScriptHeaderItem.forReference(new JQueryPluginResourceReference(HomePage.class,"HomePage.js")));
		// response.render(new FilteredHeaderItem(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(HomePage.class,"top.js")),JavaScriptFilteredIntoFooterHeaderResponse.HEADER_FILTER_NAME));
		
		response.render(CssHeaderItem.forCSS(ETheme.getActiveTheme().getStyle(), "avkTheme"));
	}

	@Override
	protected void onAfterRender()
	{
		// make sure to close hibernate session to prevent java heap space issues in Wildfly
		DBHelper.closeSession();
		super.onAfterRender();
	}

}
