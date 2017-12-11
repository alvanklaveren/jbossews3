package com.myforum.forumpages;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myforum.application.AllConstants;
import com.myforum.application.CryptWithPBKDF2;
import com.myforum.application.DBHelper;
import com.myforum.application.StringLogics;
import com.myforum.base.BasePage;
import com.myforum.framework.ResponseButton;
import com.myforum.homepage.HomePage;
import com.myforum.tables.ForumUser;
import com.myforum.tables.dao.ClassificationDao;
import com.myforum.tables.dao.ForumUserDao;

public class UserRegistrationPage extends BasePage {
 	
	private static final long 	serialVersionUID 	= 1L;
	private final ForumUserDao 	forumUserDao 		= new ForumUserDao();
	
   	private static Logger log = LoggerFactory.getLogger(UserRegistrationPage.class);
	
	public UserRegistrationPage() {

		// check whether a user is active
        //final ForumUser forumUser = getForumUserFromParameters(params);
		if(getActiveUser() == null){
			setResponsePage(HomePage.class);
			return;
		}

		
		// Form for add message button
		StatelessForm<Object> form = new StatelessForm<Object>( "registrationform" ) {
			private static final long serialVersionUID = 1L;
		};

		final Label titleLabel 					= new Label( "lbl_title", 	 	 new Model<String>(translator.translate("User Registration")));
		final Label usernameLabel 				= new Label( "lbl_username", 	 new Model<String>(translator.translate("Username")));
		final Label passwordLabel				= new Label( "lbl_password", 	 new Model<String>(translator.translate("Password")));
		final Label emailAddressLabel 			= new Label( "lbl_emailaddress", new Model<String>(translator.translate("Email")));
		
		final TextField<String> usernameTF 		= new TextField<String>( "username",	 new Model<String>() ); 
		final PasswordTextField passwordTF 		= new PasswordTextField( "password", 	 new Model<String>() ); 
		final TextField<String> emailaddressTF 	= new TextField<String>( "emailaddress", new Model<String>() ); 
			
	    Button registerButton = new Button( "register", new Model<String>(translator.translate("Register")) ) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {	        	
	        	String username 	= usernameTF.getInput().trim();
	        	String password 	= passwordTF.getInput().trim();
	        	String emailAddress	= emailaddressTF.getInput().trim();
	        	
	    		if( StringLogics.isEmpty( password ) || StringLogics.isEmpty( username ) ){
	    			setErrorMessage( "Username and/or password is empty" );
	    			setResponsePage( ForumBasePage.class );
		        	return;
	    		}
	    
	    		// TODO: Can be replaced by an EmailValidator, using a FeedBack panel to report invalid emailaddress
	    		if( StringLogics.isEmpty( emailAddress ) || !StringLogics.validateEmailAddress( emailAddress ) ){
	    			setErrorMessage( "Please enter a valid emailaddress" );
	    			setResponsePage( ForumBasePage.class );
		        	return;	    			
	    		}

	    		try {
					password = CryptWithPBKDF2.generateStrongPasswordHash(password);
				} catch (Exception e) {
					e.printStackTrace();
					log.error("Failed to encrypt password: " + e.getMessage());
	        		setResponsePage( ForumBasePage.class );
					return;
					
				}

	    		if ( forumUserDao.findByUsername( username ) != null ){
	    			setErrorMessage( "Username " + username + " already exists. Please choose a different username." );
	        		setResponsePage( ForumBasePage.class );
	        		return;
	        	}

	    		if ( forumUserDao.findByEmailAddress( emailAddress ) != null ){
	    			setErrorMessage( "email address " + emailAddress + " already exists. Use different emailaddress or go to 'forgot password' to get our credentials by email." );
	        		setResponsePage( ForumBasePage.class );
	        		return;
	        	}

	        	ForumUser newForumUser = new ForumUser();
	        	newForumUser.setUsername( username );
	        	newForumUser.setDisplayName( username );
	        	newForumUser.setPassword( password );
	        	newForumUser.setClassification( new ClassificationDao().find(AllConstants.GUEST) ); 
       	
	        	DBHelper.saveAndCommit(newForumUser);
	        	
	        	setResponsePage( ForumBasePage.class );
	        	return;
	        }
	    };
	    registerButton.setDefaultFormProcessing(false);

	    Button cancelButton = new ResponseButton("cancel", new Model<String>(translator.translate("Cancel")), ForumBasePage.class);

		add( titleLabel );
		
		form.add( usernameLabel );
		form.add( passwordLabel );
		form.add( emailAddressLabel );
		form.add( usernameTF );
		form.add( passwordTF );
		form.add( emailaddressTF );					
	    form.add( registerButton );	    
		form.add( cancelButton );
		add( form );
	}	
}