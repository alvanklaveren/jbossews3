package com.myforum.forumpages;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.model.PropertyModel;

import com.myforum.application.CookieLogics;
import com.myforum.application.DBHelper;
import com.myforum.application.ForumUtils;
import com.myforum.tables.Message;
import com.myforum.tables.MessageCategory;
import com.myforum.tables.dao.MessageCategoryDao;
import com.myforum.tables.dao.MessageDao;

public class MoveMessagePanel extends ForumBasePanel {
	
	private static final long 			serialVersionUID 	= 1L;
	private final MessageDao			messageDao 			= new MessageDao();				
	private final MessageCategoryDao	messageCategoryDao 	= new MessageCategoryDao();				
	
	public MoveMessagePanel(final ForumBasePage parent){
		super(parent);
		
		final int codeMessage = CookieLogics.getCookieInt("codeMessage");
        if(codeMessage <= 0){
        	CookieLogics.deleteCookie("codeMessageCategory");
        	CookieLogics.deleteCookie("codeMessage");
        	setResponsePage(ForumBasePage.class);
        	return;
		}

		final Message 			message 				= messageDao.find( codeMessage );
    	final MessageCategory 	originalMessageCategory = message.getMessageCategory();
    	DateFormat 				dateFormat 				= new SimpleDateFormat( "dd-MM-yyyy" );
    	
		add( new Label( "description", 			message.getDescription()).setEscapeModelStrings(true) );
		add( new Label( "messagedate", 			dateFormat.format(message.getMessageDate() ) ) );
		add( new Label( "username", 			message.getForumUser().getUsername() ) );
		add( new MultiLineLabel( "messagetext", message.getMessageText())/*.setEscapeModelStrings(false)*/);
		add( ForumUtils.loadAvatar( message.getForumUser(), "avatarpicture"));
				
		final StatelessForm<Object> form = new StatelessForm<Object>( "formcategory" ) {
			private static final long serialVersionUID = 1L;
		};
	
		List<MessageCategory> listMessageCategories;
		// only administrator or the owner of the message can move a message
		if ( allowModification(message.getForumUser()) ) {
			listMessageCategories = messageCategoryDao.list();	
		}else{
			// only add the current category
			listMessageCategories = Arrays.asList( originalMessageCategory );
		}
	
		DropDownChoice<MessageCategory> listMessageCategoriesDDC =  
			new DropDownChoice<MessageCategory>( "forumcategories",	new PropertyModel<MessageCategory>( message, "messageCategory" ), listMessageCategories	); 
		
		// Need a choice renderer to be able to set the selected item in the list to the current forumuser's classification 
		listMessageCategoriesDDC.setChoiceRenderer( new IChoiceRenderer<MessageCategory>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Object getDisplayValue( MessageCategory messageCategory ) {	
					return messageCategory.toString();
				}

				@Override
				public String getIdValue( MessageCategory messageCategory, int index ) {
					return String.valueOf( messageCategory.getCode() );
				}
            });
			
		listMessageCategoriesDDC.setModelObject( message.getMessageCategory() );

		listMessageCategoriesDDC.add(new OnChangeAjaxBehavior() {               
	            private static final long serialVersionUID = 1L; 
	
	            @Override 
	            protected void onUpdate( AjaxRequestTarget target ) { 
	            	// Because we want to update the value immediately and don't want
	            	// to wait until the page renders again, force an Ajax onUpdate event
	            	message.setMessageCategory( message.getMessageCategory() );
	            } 
	    });
		
		form.add( listMessageCategoriesDDC );
		
	    Button moveMessageButton = new Button( "movemessage" ) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {				
				if( !DBHelper.saveAndCommit(message)){
					setErrorMessage("Failed to move message to new category");
					parent.addOrReplace(new MoveMessagePanel(parent));
				}
          	
            	// and move back to original Category (the one you started in)
				CookieLogics.deleteCookie("codeMessage");
		       	parent.addOrReplace(new ForumCategoryPanel(parent));
  	        	return;
	        }
	    };
	    moveMessageButton.setDefaultFormProcessing( false );
	    form.add( moveMessageButton );
	    
	    add( form );
	}
}
