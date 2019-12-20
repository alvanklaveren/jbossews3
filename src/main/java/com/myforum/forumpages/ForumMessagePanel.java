package com.myforum.forumpages;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.Model;

import com.myforum.application.CookieLogics;
import com.myforum.application.DBHelper;
import com.myforum.application.ForumUtils;
import com.myforum.application.StringLogics;
import com.myforum.framework.AreYouSurePanel;
import com.myforum.framework.ResponseFormButton;
import com.myforum.tables.ForumUser;
import com.myforum.tables.Message;
import com.myforum.tables.MessageImage;
import com.myforum.tables.dao.ForumUserDao;
import com.myforum.tables.dao.MessageDao;
import com.myforum.tables.dao.MessageImageDao;

public class ForumMessagePanel extends ForumBasePanel {

	private static final long 	serialVersionUID = 1L;

	private final SimpleDateFormat 		dateFormat 			= new SimpleDateFormat("dd-MM-yyyy");
	private final MessageDao		 	messageDao 			= new MessageDao();
	private final ForumUserDao 			forumUserDao 		= new ForumUserDao();
	
	private ForumMessagePanel thisPanel = null;

	public ForumMessagePanel(final ForumBasePage parent){
		super(parent);
        final int codeMessage = CookieLogics.getCookieInt("codeMessage");

        thisPanel = this;
        
        if(codeMessage == 0){
        	setResponsePage(ForumBasePage.class);
        	return;
        }
        
		final Message 	message 	 = messageDao.find( codeMessage );
        final ForumUser messageOwner = forumUserDao.find( message );
        
		// Form for [move message] button
        StatelessForm<Object> form = createButtonForm(message);
		
		final MultiLineLabel messageTextLabel = new MultiLineLabel( "messagetext", StringLogics.prepareMessage(message.getMessageText()) );
		messageTextLabel.setEscapeModelStrings( false );
			
		// Form for reply
		final Form<Object> newMessageForm = new ForumMessageForm( "newmessageform", getActiveUser(), parent);
		newMessageForm.setVisible( false );

       	final Label editTextLabel = new Label("edittext", new Model<String>("[Edit]") );
       	
       	final Form<Object> editMessageForm = createEditMessageForm(message, messageTextLabel, editTextLabel); 

       	editTextLabel.setVisible( getActiveUser() != null && getActiveUser().getCode() > 0 && allowModification(message.getForumUser()) );
        editTextLabel.add( new AjaxEventBehavior("onclick") {
			private static final long serialVersionUID = 1L;
			@Override
	        protected void onEvent(AjaxRequestTarget target) {
				editMessageForm.setVisible( true );
				editTextLabel.setVisible( false );
				messageTextLabel.setVisible( false );
				target.add(thisPanel);
            }
        } );
       	
		AreYouSurePanel deletePanel = new AreYouSurePanel( "deletePanel", "Delete Message", "Do you really want to delete this message?" ) { 
			private static final long serialVersionUID = 1L;

			@Override
            protected void onConfirm(AjaxRequestTarget target) {
				// delete message
				List<Message> threadMessages = messageDao.getThreadMessages(message);
				for(Message thread:threadMessages){
					if(!DBHelper.deleteAndCommit(thread)){
						parent.setErrorMessage("Failed to delete thread messages");
						target.add( new ForumMessagePanel(parent) );
						return;					
					}
				}
				
				if( !DBHelper.deleteAndCommit(message) ){
					parent.setErrorMessage("Failed to delete message");
					target.add( new ForumMessagePanel(parent) );
					return;
				}

				// and return to forum category since message is deleted
				CookieLogics.deleteCookie("codeMessage");
				setResponsePage(new ForumBasePage());
				return;
            }
 
            @Override
            protected void onCancel(AjaxRequestTarget target) { }
 
        };
        //TODO: The delete button was not properly hiding... need to figure this out
       	deletePanel.setVisible(getActiveUser() != null && getActiveUser().getCode() > 0 && allowModification(message.getForumUser()));
    
        final Label addCommentLabel = new Label("addcomment", new Model<String>("Add Comment"));
        addCommentLabel.add( new AjaxEventBehavior("onclick") {
			private static final long serialVersionUID = 1L;
			@Override
	        protected void onEvent(AjaxRequestTarget target) {
				newMessageForm.setVisible( true );
				addCommentLabel.setVisible( false );
				target.add(thisPanel);
	        }
	    });
       
        addCommentLabel.setVisible( getActiveUser() != null && getActiveUser().getCode() > 0 );
        
        PropertyListView<Message> threadMessageListView = createThreadMessageView(message);
        
        int numberOfAnswers = threadMessageListView.getList().size();
        add( new Label("numberofanswers", new Model<String>(String.valueOf(numberOfAnswers))) );

        add( new Label( "description", 			message.getDescription() ) );
		add( new Label( "messagedate", 			dateFormat.format(message.getMessageDate() ) ) );
		add( new Label( "userdisplayname", 		messageOwner.getDisplayName() ) );

		add(form);
		add( ForumUtils.loadAvatar( messageOwner, "avatarpicture" ) );
		add( messageTextLabel );
		add( newMessageForm );
		add( editMessageForm );
        add( editTextLabel );
        add( threadMessageListView );
    	add( deletePanel ); 
        add( addCommentLabel );
	}
	
	/*
	 *  Form containing the [move message] button
	 */
	private StatelessForm<Object> createButtonForm(Message message){
		// Form for move message button
        StatelessForm<Object> form = new StatelessForm<Object>("form");

        ResponseFormButton button = new ResponseFormButton("movemessage", "Move Message To Other Category", parent){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible(){
        		return parent.getActiveUser() != null;
        	}
			
			@Override
			public void onSubmit() {
				parent.addOrReplace(new MoveMessagePanel(parent));
		    }
        };
	    form.add(button);
		form.setVisible( getActiveUser() != null && getActiveUser().getCode() > 0 && allowModification(message.getForumUser()) );
		return form;
	}
	
	/*
	 *  Form containing the functionality to edit the message (when you are the owner of that message)
	 */
	private Form<Object> createEditMessageForm(final Message message, final MultiLineLabel messageTextLabel, final Label editTextLabel){
		final Form<Object> editMessageForm = new Form<Object>( "editmessageform" );
		editMessageForm.setVisible( false );
		
		final TextArea<String> editMessageTextTA = new TextArea<String>( "editmessagetext", new Model<String>( message.getMessageText() ) );

	    Button saveButton = new Button("saveedit") {
			private static final long serialVersionUID = 1L;    
				
			@Override
			public void onSubmit() {
				message.setMessageText(editMessageTextTA.getInput());
				if( DBHelper.saveAndCommit(message) == null ){
					parent.setErrorMessage("Failed to save message");
				}

				// uploaded images are only linked once the message is saved. 
				// this makes it easier to clean up the message_image table where all 'code_message' columns are null
				ForumUtils.linkImages(message);		
				
				messageTextLabel.setDefaultModel( new Model<String>( StringLogics.prepareMessage( message.getMessageText() ) ) );
				editMessageForm.setVisible( false );
				editTextLabel.setVisible( true );
				messageTextLabel.setVisible( true );
        		parent.addOrReplace(new ForumMessagePanel(parent));
		    }
	    };
	    saveButton.setDefaultFormProcessing(false);

	    Button cancelButton = new Button("canceledit") {
			private static final long serialVersionUID = 1L;    
				
			@Override
			public void onSubmit() {
				editMessageForm.setVisible( false );
				editTextLabel.setVisible( true );
				messageTextLabel.setVisible( true );
        		parent.addOrReplace(new ForumMessagePanel(parent));
		    }
	    };
	    cancelButton.setDefaultFormProcessing(false);
    	
	    editMessageForm.add( editMessageTextTA );		
		editMessageForm.add( saveButton );
	    editMessageForm.add( cancelButton );
	    editMessageForm.setOutputMarkupId(true);
	    
	    return editMessageForm;
	}
	
	/*
	 *  View containing all the reply-messages to this message
	 */
	private PropertyListView<Message> createThreadMessageView(final Message message){
		List<Message> threadMessageList = messageDao.getThreadMessages( message );
		
		PropertyListView<Message> threadMessageListView = new PropertyListView<Message>( "threadmessagelist", threadMessageList ) {
			private static final long serialVersionUID = 1L;

			@Override
            public void populateItem(final ListItem<Message> listItem) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				
				final Message 	threadMessage 	= listItem.getModelObject();
				final ForumUser	threadForumUser =  forumUserDao.find( threadMessage );
				
                final MultiLineLabel replyMessageText = new MultiLineLabel( "replymessage", StringLogics.prepareMessage( threadMessage.getMessageText() ) );
                replyMessageText.setEscapeModelStrings(false);

        		final Form<Object> editReplyMessageForm = new Form<Object>( "editreplymessageform" );  
        		editReplyMessageForm.setVisible( false );

        		final TextArea<String> editReplyMessageTextTA = new TextArea<String>( "editreplymessagetext", new Model<String>( threadMessage.getMessageText() ) );
        		editReplyMessageTextTA.setOutputMarkupId(true);

        	    Button saveEditReplyButton = new Button( "saveeditreply" ) {
        			private static final long serialVersionUID = 1L;    
        				
        			@Override
        			public void onSubmit() {
        				threadMessage.setMessageText( editReplyMessageTextTA.getInput() );
        				
        				if( DBHelper.saveAndCommit(threadMessage) == null ){
        					parent.setErrorMessage( "Failed to save message" );
        					parent.addOrReplace(new ForumMessagePanel(parent));
        					return;
        				}

           				replyMessageText.setDefaultModel( new Model<String>( StringLogics.prepareMessage( editReplyMessageTextTA.getInput() ) ) );
           				editReplyMessageForm.setVisible( false );
           				replyMessageText.setVisible( true );          		
    					parent.addOrReplace(new ForumMessagePanel(parent));
        		    }
        	    };

        	    saveEditReplyButton.setDefaultFormProcessing( false );
        	    
        	    Button cancelButton = new Button( "canceleditreply" ) {
        			private static final long serialVersionUID = 1L;    
        				
        			@Override
        			public void onSubmit() {
        				editReplyMessageForm.setVisible( false );
        				replyMessageText.setVisible( true );
        				parent.renderPage();
        		    }
        	    };
        	    cancelButton.setDefaultFormProcessing( false );

        		editReplyMessageForm.add( editReplyMessageTextTA );		
        	    editReplyMessageForm.add( saveEditReplyButton );
        	    editReplyMessageForm.add( cancelButton );
        		editReplyMessageForm.setOutputMarkupId( true );
      		       		
                AreYouSurePanel deleteThreadPanel = new AreYouSurePanel( "deleteThreadPanel", "Delete Thread Message", "Do you really want to delete this message?" ) { 
        			private static final long serialVersionUID = 1L;

        			@Override
                    protected void onConfirm(AjaxRequestTarget target) {
        				if(!DBHelper.deleteAndCommit(threadMessage)){
        					parent.setErrorMessage("Failed to delete message");
        				}
        				
    					setResponsePage(new ForumBasePage());
                		return;
                    }
        			        
                    @Override
                    protected void onCancel(AjaxRequestTarget target) { }
         
                };
                //TODO: The delete button was not properly hiding... need to figure this out
               	deleteThreadPanel.setVisible(getActiveUser() != null && getActiveUser().getCode() > 0 && allowModification(message.getForumUser()));
                
        		final Label editReplyTextLabel = new Label( "editreplytext", new Model<String>("Edit") );
        		editReplyTextLabel.setOutputMarkupId( true );
        		editReplyTextLabel.setVisible(getActiveUser() != null && getActiveUser().getCode() > 0 && allowModification(message.getForumUser()));
        		
               	if(allowModification(threadMessage.getForumUser())){
        			editReplyTextLabel.setVisible( false );
                }
        	    
        	    editReplyTextLabel.add( new AjaxEventBehavior("onclick") {
        			private static final long serialVersionUID = 1L;
        			
        			@Override
        	        protected void onEvent(AjaxRequestTarget target) {
        				editReplyTextLabel.setVisible( false );
        				replyMessageText.setVisible( false );
        				editReplyMessageForm.setVisible( true );
        				target.add(thisPanel);
        	        }
                } );

        	    listItem.add( new Label("replydate", 			dateFormat.format( threadMessage.getMessageDate()) ));
                listItem.add( new Label("replyuserdisplayname", threadForumUser.getDisplayName()));
                listItem.add( ForumUtils.loadAvatar( threadForumUser, "threadavatarpicture") );
                listItem.add( replyMessageText );
        		listItem.add( editReplyMessageForm );
        		listItem.add( editReplyTextLabel );
                listItem.add( deleteThreadPanel ); 

			}
        };   
        
        // setReuseItems( true ) is very important. Each time a page renders, the listview is rebuild with new IDs, making it impossible 
        // to address the items for, say, visibility. 
        threadMessageListView.setReuseItems( true ); 

		return threadMessageListView;
	}
}
