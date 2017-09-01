package com.myforum.forumpages.header;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.StatelessLink;
import org.apache.wicket.model.Model;

import com.myforum.application.CookieLogics;
import com.myforum.base.BasePage;
import com.myforum.forumpages.ForgotPasswordPanel;
import com.myforum.forumpages.ForumBasePage;
import com.myforum.security.CredentialLogics;
import com.myforum.tables.ForumUser;
import com.myforum.tables.dao.ForumUserDao;
import com.myforum.tables.dao.LoginCredDao;

public class LoginForm extends StatelessForm<Object>{
	private static final long serialVersionUID = 1L;
	
	private ForumUser activeUser;

	public LoginForm( String componentName, final ForumUser activeUser ){
		// Form for login credentials
		super( componentName );
		
		this.activeUser = activeUser;
		
		final TextField<String> usernameTF = new TextField<String>( "username", new Model<String>() );
		usernameTF.setModel( new Model<String>( (String) getSession().getAttribute( "loginname" ) ) );
		add( usernameTF );
		
		usernameTF.add( new OnChangeAjaxBehavior(){
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onUpdate( AjaxRequestTarget target ) {
				// Override normal behavior so onclick event in forgotPasswordLabel is able to get the modelobject from usernameTF component.
			}
		});
		
		final PasswordTextField passwordTF = new PasswordTextField( "password", new Model<String>() ); 
		add( passwordTF );
		
		final StatelessLink<Object> forgotPasswordLink = new StatelessLink<Object>( "forgotpassword" ){
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
		        synchronized(this){
		        	@SuppressWarnings("unchecked")
					TextField<String> aTF = (TextField<String>) this.getParent().get("username");
		        	getSession().setAttribute("loginname", aTF.getModelObject());
					ForumBasePage parent = (ForumBasePage) getPage();
					parent.addOrReplace(new ForgotPasswordPanel(parent));
		        }			
			}
			
		};
		forgotPasswordLink.add(new Label("forgotlabel", new Model<String>("Forgot password?")));
        add( forgotPasswordLink );
					
	    Button loginButton = new Button( "login", new Model<String>("Login") ) {
			private static final long serialVersionUID = 1L;
	
			@Override
			public void onSubmit() {
	        	String username = usernameTF.getInput();
	        	String password = passwordTF.getInput();
        	    	        	        	
	        	if(!CredentialLogics.validCredentials(username, password)){
	        		((BasePage) getPage()).setErrorMessage( "Incorrect login credentials" );
	        		CookieLogics.deleteCookie("magictoken");
	        	}else{  	      	
		        	// user name + password valid. Connect user to forum.
	        		ForumUserDao forumUserDao 	= new ForumUserDao();
	        		ForumUser 	 user 			= forumUserDao.findByUsername(username);
	        		String 		 magicToken 	= forumUserDao.generateMagicToken(user);

	        		new LoginCredDao().setMagicToken(user, magicToken);
	        		CookieLogics.setCookie("magictoken", magicToken);
	
		        	usernameTF.clearInput();
		        	passwordTF.clearInput();
	        	}

	        	setResponsePage( getPage().getClass() );
	        	return;
			}
	    };
	    loginButton.setDefaultFormProcessing( false );
	    add( loginButton );  
	    
	}
	
    @Override
    public boolean isVisible(){
    	if( activeUser == null ){
    		return true;
    	}
    	return false;
    }
}
