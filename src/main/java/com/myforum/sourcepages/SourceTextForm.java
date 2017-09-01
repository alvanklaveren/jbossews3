package com.myforum.sourcepages;

import java.util.Date;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;

import com.myforum.application.DBHelper;
import com.myforum.application.ForumUtils;
import com.myforum.application.StringLogics;
import com.myforum.base.BasePage;
import com.myforum.tables.ForumUser;
import com.myforum.tables.Message;
import com.myforum.tables.MessageCategory;
import com.myforum.tables.dao.ForumUserDao;
import com.myforum.tables.dao.MessageCategoryDao;
import com.myforum.tables.dao.MessageDao;

public class SourceTextForm extends StatelessForm<Object> {

	private static final long serialVersionUID = 1L;
	
	public SourceTextForm( String id ) {
		super( id );

        if( getSession().getAttribute( "codeMessageCategory" ) == null || getSession().getAttribute( "codeMessage" ) == null ){
            setResponsePage( SourceHomePage.class );   
            return;
        }
        
		final TextField<String> descriptionTF = new TextField<String>("replydescription" ); 	
		final TextArea<String> messageTextTA  = new TextArea<String>("replymessage" );

		descriptionTF.setModel( new Model<String>( (String) getSession().getAttribute( "messageDescription" ) ) );
		messageTextTA.setModel( new Model<String>( (String) getSession().getAttribute( "messageText" ) ) );

	    Button cancelButton = new Button( "cancel" ) {
			private static final long serialVersionUID = 1L;    
				
			@Override
			public void onSubmit() {
		        int	codeMessage	= (Integer) getSession().getAttribute( "codeMessage" );
        		getSession().setAttribute("messageDescription", "" );
        		getSession().setAttribute("messageText", "" );

        		if ( codeMessage == 0 ){
	        		setResponsePage( SourceCategoryPage.class );
	        		return;
	        	}else{
	        		setResponsePage( SourceSubjectPage.class );
	        		return;
	        	}
			}
	    };
	    cancelButton.setDefaultFormProcessing( false );
	    add( cancelButton );
		
	    Button addButton = new Button( "submit" ) {
			private static final long serialVersionUID = 1L;    
				
			@Override
			public void onSubmit() {
				int 	codeForumUser 		= 0;
		        int 	codeMessageCategory = (Integer) getSession().getAttribute( "codeMessageCategory" );
		        int		codeMessage			= (Integer) getSession().getAttribute( "codeMessage" );
				boolean isError 			= false;

				Message newMessage 			= new Message();
				newMessage.setDescription( descriptionTF.getInput() );
				newMessage.setMessageText( messageTextTA.getInput() );
				
		    	if( ForumUtils.isNullOrZero( (Integer) getSession().getAttribute( "codeforumuser" ) ) ){
	        		((BasePage) getPage()).setErrorMessage( "You have to be logged in to write messages" );
    				isError = true;
    			}   			
		    	    			
    			// only check for a title in new messages. Comments do not have a title.
    			if( codeMessage == 0 ){
					if( StringLogics.isEmpty( newMessage.getDescription() ) ){
						((BasePage) getPage()).setErrorMessage( "Please add a title" );
		        		isError = true;
					}else{
		    			getSession().setAttribute( "messageDescription", newMessage.getDescription() );
		        	}
    			}

    			
				if( StringLogics.isEmpty( newMessage.getMessageText() ) ){
					((BasePage) getPage()).setErrorMessage( "Please add message content" );
	        		isError = true;
				}else{
	    			getSession().setAttribute( "messageText", newMessage.getMessageText() );
	        	}
	        	        	    			    			    			
    			if( !isError ){ 
    		    	codeForumUser = (Integer) getSession().getAttribute( "codeforumuser" );

    				MessageCategory messageCategory = new MessageCategoryDao().find( codeMessageCategory );
    		    	MessageDao		messageDao		= new MessageDao();
    				ForumUserDao 	forumUserDao 	= new ForumUserDao();		
    				ForumUser 		forumUser 		= forumUserDao.find( codeForumUser );
    				
    				newMessage.setMessage(			messageDao.find(codeMessage) );
    		    	newMessage.setMessageCategory( 	messageCategory );
    		    	newMessage.setMessageDate( 		new Date() );
	    	    	newMessage.setForumUser( 		forumUser );
	
					if( messageDao.add( newMessage ) ){
						forumUserDao.addMessage( forumUser, newMessage );
		        		new MessageCategoryDao().addMessage( newMessage.getMessageCategory(), newMessage );

		        		DBHelper.getTransaction().commit();
					}else{
						DBHelper.getTransaction().rollback();
					}

    			}
      	
        		setResponsePage( SourceCategoryPage.class );
        		return;
	        }
	    
	    };    
	    addButton.setDefaultFormProcessing( false );

		add( descriptionTF );
		add( messageTextTA );		
	    add( addButton );    	
	}

}
