package com.myforum.forumpages.header;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.myforum.application.AllConstants;
import com.myforum.application.CookieLogics;
import com.myforum.forumpages.ForumBasePage;
import com.myforum.forumpages.UserModifyAccountPage;
import com.myforum.forumpages.UserRegistrationPage;
import com.myforum.forumpages.administrator.ForumAdministratorPage;
import com.myforum.framework.ResponseButton;
import com.myforum.security.CredentialLogics;
import com.myforum.tables.ForumUser;

public class LogoutForm extends StatelessForm<Object>{
	private static final long serialVersionUID = 1L;

	private ForumUser activeUser;

	public LogoutForm(String componentName, final ForumUser activeUser){
		// Form for when user is logged in 
		super( componentName );

		this.activeUser = activeUser;
		if( activeUser == null ) return;

	    boolean isAdministrator = CredentialLogics.isAdministrator(activeUser);
	    boolean isMember 		= CredentialLogics.isMember(activeUser);

		Label activeUserLabel = new Label( "activeuser", activeUser.getDisplayName() + " is logged in" );
		add( activeUserLabel );

		ResponseButton adminButton = new ResponseButton( "adminmaintenance", ForumAdministratorPage.class );
		add( adminButton );
		adminButton.setVisible(isAdministrator);
	
		Button updateProfileButton = new Button( "updateprofile" );
		updateProfileButton.add( new AjaxEventBehavior( "onclick" ) {
			private static final long serialVersionUID = 1L;
			@Override
            protected void onEvent( AjaxRequestTarget target ) {
				String urlEncoded = AllConstants.getCrypt().encryptUrlSafe( String.valueOf(activeUser.getCode()) );
				PageParameters parameters = new PageParameters().set("codeforumuser", urlEncoded);
                setResponsePage( UserModifyAccountPage.class, parameters );
            }
        });		
		add( updateProfileButton );

		Button logoutButton = new Button( "logout" );
		logoutButton.add( new AjaxEventBehavior( "onclick" ) {
			private static final long serialVersionUID = 1L;
			@Override
            protected void onEvent( AjaxRequestTarget target ) {
				CookieLogics.deleteCookie("magictoken");
				getSession().invalidateNow(); // remember that now the session is invalidated, all getsession-variables are reset to null
				setResponsePage( ForumBasePage.class );
				return;
            }
        });		
		add( logoutButton );
	
		ResponseButton userRegistrationButton = new ResponseButton( "userregistration", UserRegistrationPage.class );
	    add( userRegistrationButton );
		userRegistrationButton.setVisible(isAdministrator || isMember);
	    	
	}

    @Override
    public boolean isVisible(){
    	if( activeUser != null ) return true;

    	return false;
    }
}
