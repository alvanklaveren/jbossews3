package com.myforum.forumpages;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myforum.application.DBHelper;
import com.myforum.application.StringLogics;
import com.myforum.dictionary.EText;
import com.myforum.email.HotmailMessage;
import com.myforum.framework.AVKButton;
import com.myforum.tables.ForumUser;
import com.myforum.tables.dao.ForumUserDao;

public class ForgotPasswordPanel extends ForumBasePanel {

	private static final long 	serialVersionUID 	= 1L;
	private final ForumUserDao 	forumUserDao 		= new ForumUserDao();
	
	private static Logger log = LoggerFactory.getLogger(ForgotPasswordPanel.class);
	
	public ForgotPasswordPanel(final ForumBasePage parent) {
		super(parent);

		// Form for add message button
		StatelessForm<Object> form = new StatelessForm<Object>( "forgotpasswordform" ) {
			private static final long serialVersionUID = 1L;
		};

		String loginname = ""; // this is currently not working because of onclick not accepting variables in loginform
        if( getSession().getAttribute( "loginname" ) != null ){
        	loginname = (String) getSession().getAttribute( "loginname" );
        }     
        
		final TextField<String> usernameOrEmailTF = new TextField<String>( "usernameoremail", new Model<String>( loginname ) );
		form.add( usernameOrEmailTF );
		
	    Button sendPasswordButton = new AVKButton( "sendpassword", "Send Password" ) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {	        	
	        	String usernameOrEmail 	= usernameOrEmailTF.getInput();        	
	        	ForumUser forumUser 	= forumUserDao.findByUsername( usernameOrEmail );
	    		
	        	if( forumUser == null ){
		        	forumUser = forumUserDao.findByEmailAddress( usernameOrEmail );	        		
	        	}
	        	
	        	if( forumUser == null ){
	        		setResponsePage(ForumBasePage.class);
	        		return;
	        	}
	        	
        		String newPassword = forumUserDao.generateNewPassword(forumUser);
        		if(StringLogics.isEmpty(newPassword)){
        			DBHelper.getTransaction().rollback();
        			parent.setErrorMessage( EText.FAILED_GENERATE_PASSWORD );
        			setResponsePage(ForumBasePage.class);
        			return;
        		}  
        		
                if( DBHelper.saveAndCommit(forumUser)){
                	emailPassword(forumUser.getEmailAddress(), forumUser.getUsername(), newPassword);
                }else{
                	parent.setErrorMessage( EText.FAILED_CHANGE_PASSWORD );
				}
	        	
	        	setResponsePage(ForumBasePage.class);
	        	return;
	        }
	    };
	    
	    sendPasswordButton.setDefaultFormProcessing( false );
	    form.add( sendPasswordButton );
	    
		add( form );	
	}	
	
	public void emailPassword(String emailAddress, String username, String newPassword){
        HotmailMessage hotmailMessage = new HotmailMessage(emailAddress);
        hotmailMessage.setSubject("Your password at alvanklaveren.com has been reset");
        hotmailMessage.setBody("Username: " + username + "\nPassword: " + newPassword);

        try {
        	hotmailMessage.send();
        } catch (AddressException e) {
        	parent.setErrorMessage( EText.UNABLE_SEND_EMAIL );
	    	log.error("AddressException: " + e.getMessage());
			e.printStackTrace();
        } catch (MessagingException e) {
        	parent.setErrorMessage( EText.UNABLE_SEND_EMAIL );
	    	log.error("MessagingException: " + e.getMessage());
			e.printStackTrace();
        }
        
        parent.setSuccessMessage( EText.EMAIL_SENT );
	}
}