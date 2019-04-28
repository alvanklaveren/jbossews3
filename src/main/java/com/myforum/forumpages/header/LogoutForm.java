package com.myforum.forumpages.header;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.myforum.application.AllConstants;
import com.myforum.application.CookieLogics;
import com.myforum.dictionary.Translator;
import com.myforum.forumpages.ForumBasePage;
import com.myforum.forumpages.UserModifyAccountPage;
import com.myforum.forumpages.UserRegistrationPage;
import com.myforum.forumpages.administrator.ForumAdministratorPage;
import com.myforum.framework.AVKButton;
import com.myforum.framework.ResponseButton;
import com.myforum.security.CredentialLogics;
import com.myforum.tables.ForumUser;

public class LogoutForm extends StatelessForm<Object>{
	private static final long serialVersionUID = 1L;

	private ForumUser activeUser;
	private Translator translator = Translator.getInstance();

	public LogoutForm(String componentName, final ForumUser activeUser){
		// Form for when user is logged in 
		super( componentName );

		this.activeUser = activeUser;
		if( activeUser == null ) return;

	    boolean isAdministrator = CredentialLogics.isAdministrator(activeUser);
	    boolean isMember 		= CredentialLogics.isMember(activeUser);

		Label activeUserLabel = new Label( "activeuser", translator.translate( "Welcome" ) + " " + activeUser.getDisplayName() );
		add( activeUserLabel );

		ResponseButton adminButton = new ResponseButton( "adminmaintenance", "Administrator Maintenance", ForumAdministratorPage.class );
		add( adminButton );
		adminButton.setVisible(isAdministrator);
	
		Button updateProfileButton = new AVKButton( "updateprofile", "Update Profile" ) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit() {
				String urlEncoded = AllConstants.getCrypt().encryptUrlSafe( String.valueOf(activeUser.getCode()) );
				PageParameters parameters = new PageParameters().set("codeforumuser", urlEncoded);
                setResponsePage( UserModifyAccountPage.class, parameters );
            }
        };		
        updateProfileButton.setDefaultFormProcessing( false );
		add( updateProfileButton );

		Button logoutButton = new AVKButton( "logout", "Logout" ) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit() {
				CookieLogics.deleteCookie("magictoken");
				getSession().invalidateNow(); // remember that now the session is invalidated, all getsession-variables are reset to null
				setResponsePage( ForumBasePage.class );
				return;
            }
        };		
        logoutButton.setDefaultFormProcessing( false );
	    add( logoutButton );
	
		ResponseButton userRegistrationButton = new ResponseButton( "userregistration", "Register New User", UserRegistrationPage.class );
	    add( userRegistrationButton );
		userRegistrationButton.setVisible(isAdministrator || isMember);
	    	
	}

    @Override
    public boolean isVisible(){
    	if( activeUser != null ) return true;

    	return false;
    }
}
