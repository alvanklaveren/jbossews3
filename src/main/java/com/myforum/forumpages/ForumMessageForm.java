package com.myforum.forumpages;

import java.util.Date;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;

import com.myforum.application.CookieLogics;
import com.myforum.application.DBHelper;
import com.myforum.application.StringLogics;
import com.myforum.base.AVKPage;
import com.myforum.tables.ForumUser;
import com.myforum.tables.Message;
import com.myforum.tables.MessageCategory;
import com.myforum.tables.dao.MessageCategoryDao;
import com.myforum.tables.dao.MessageDao;

public class ForumMessageForm extends StatelessForm<Object> {

	private static final long serialVersionUID = 1L;
	
	public ForumMessageForm( String id, final ForumUser activeUser, final ForumBasePage parent ) {
		super( id );

        if( CookieLogics.getCookieInt("codeMessageCategory") == 0 ){
        	CookieLogics.deleteCookie("codeMessageCategory");
        	CookieLogics.deleteCookie("codeMessage");
        	setResponsePage(ForumBasePage.class);		
			return;
        }
        
		final TextField<String> descriptionTF = new TextField<String>("replydescription"); 	
		final TextArea<String> messageTextTA  = new TextArea<String>("replymessage");

		descriptionTF.setModel( new Model<String>( (String) getSession().getAttribute("messageDescription") ) );
		messageTextTA.setModel( new Model<String>( (String) getSession().getAttribute("messageText") ) );

	    Button cancelButton = new Button( "cancel" ) {
			private static final long serialVersionUID = 1L;    
				
			@Override
			public void onSubmit() {
		        int codeMessage = CookieLogics.getCookieInt("codeMessage");
		        if(codeMessage > 0){
	        		getSession().setAttribute("messageDescription", "");
	        		getSession().setAttribute("messageText", "");
				}
	        	parent.addOrReplace(new ForumMessagePanel(parent));
	        	return;
			}
	    };
	    cancelButton.setDefaultFormProcessing( false );
	    add( cancelButton );
		
	    Button addButton = new Button("save") {
			private static final long serialVersionUID = 1L;    
				
			@Override
			public void onSubmit() {
		        int codeMessageCategory = CookieLogics.getCookieInt("codeMessageCategory");
		        if(codeMessageCategory == 0){
		        	CookieLogics.deleteCookie("codeMessageCategory");
		        	CookieLogics.deleteCookie("codeMessage");
		        	setResponsePage(ForumBasePage.class);
		        	return;
				}

				boolean isError 	= false;
		        int 	codeMessage = CookieLogics.getCookieInt("codeMessage");

		        Message newMessage 			= new Message();
				newMessage.setDescription( descriptionTF.getInput() );
				newMessage.setMessageText( messageTextTA.getInput() );
				
				if( activeUser.getCode() <= 0 ){
	        		parent.setPanelErrorMessage( "You have to be logged in to write messages" );
    				isError = true;
    			}   			
		    	    			
    			// only check for a title in new messages. Comments do not have a title.
    			if( codeMessage == 0 ){
					if( StringLogics.isEmpty( newMessage.getDescription() ) ){
						parent.setPanelErrorMessage( "Please add a title" );
		        		isError = true;
					}else{
		    			getSession().setAttribute( "messageDescription", newMessage.getDescription() );
		        	}
    			}

				if( StringLogics.isEmpty( newMessage.getMessageText() ) ){
					parent.setPanelErrorMessage( "Please add message content" );
	        		isError = true;
				}else{
	    			getSession().setAttribute( "messageText", newMessage.getMessageText() );
	        	}
	        	        	    			    			    			
    			if( !isError ){ 
    				// If the message the reply is meant for has just been created, the session should restart to acknowledge
    				// the newly added message. Therefore, closeSession!
    				DBHelper.closeSession();
    				MessageCategoryDao 	messageCategoryDao	= new MessageCategoryDao();
    				MessageDao 			messageDao			= new MessageDao();
    				MessageCategory 	messageCategory 	= messageCategoryDao.find( codeMessageCategory );

    				if(messageDao.find(codeMessage) == null && codeMessage > 0){
	        			parent.setPanelErrorMessage("Failed to save comment");
	        			parent.addOrReplace(new ForumAddMessagePanel(parent));
	        			return;    					
    				}
   					newMessage.setMessage( messageDao.find(codeMessage) );
    		    	newMessage.setMessageCategory( messageCategory );
    		    	newMessage.setMessageDate( new Date() );
	    	    	newMessage.setForumUser( activeUser );
	
	    	    	if( !DBHelper.saveAndCommit(newMessage) ){
						parent.setPanelErrorMessage("Failed to save message");
						parent.addOrReplace(new ForumAddMessagePanel(parent));
						return;
					}
    			}
      	
    			// if the message being added was a completely new message (i.e. codeMessage == 0)... 
	        	if ( codeMessage == 0 ){
	        		if( isError ){
	        			// panel error message should now have already been populated by the above if statements
	        			parent.addOrReplace(new ForumAddMessagePanel(parent));
	        			return;
	        		}else{
		        		getSession().setAttribute("messageDescription", null);
		        		getSession().setAttribute("messageText", null);
		        		CookieLogics.deleteCookie("codeMessage");
                		parent.addOrReplace(new ForumMessagePanel(parent));
	        		}
	        	}else{
            		parent.addOrReplace(new ForumMessagePanel(parent));
            		return;
	        	}
	        }
	    
	    };    
	    addButton.setDefaultFormProcessing( false );

		add( descriptionTF );
		add( messageTextTA );		
	    add( addButton );    	
	}
}
