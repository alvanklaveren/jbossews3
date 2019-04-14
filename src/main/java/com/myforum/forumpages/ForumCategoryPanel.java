package com.myforum.forumpages;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.Model;

import com.myforum.application.CookieLogics;
import com.myforum.application.DBHelper;
import com.myforum.application.ForumUtils;
import com.myforum.base.ClickableForumLabel;
import com.myforum.framework.AreYouSurePanel;
import com.myforum.framework.ResponseFormButton;
import com.myforum.framework.StatelessPagingNavigator;
import com.myforum.tables.Message;
import com.myforum.tables.MessageCategory;
import com.myforum.tables.dao.ForumUserDao;
import com.myforum.tables.dao.MessageCategoryDao;
import com.myforum.tables.dao.MessageDao;

public class ForumCategoryPanel extends ForumBasePanel{

	private static final long 			serialVersionUID = 1L;

	private final MessageCategoryDao 	messageCategoryDao 	= new MessageCategoryDao();
	private final SimpleDateFormat		dateFormat			= new SimpleDateFormat("yyyy/MM/dd");
	private final SimpleDateFormat		timeFormat			= new SimpleDateFormat("HH:mm:ss");
	private List<Message> 				messageList 		= Collections.synchronizedList( new ArrayList<Message>() );

	private long						pageNumber = 0;
	
	public ForumCategoryPanel(final ForumBasePage parent){
		super(parent);
	
		pageNumber		= ForumUtils.getParmInt(parent.getPageParameters(),    "page", 0);

		CookieLogics.deleteCookie("codeMessage"); // just in case

        final int codeMessageCategory = CookieLogics.getCookieInt("codeMessageCategory");
        if( codeMessageCategory == 0 ){
        	setResponsePage(ForumBasePage.class);
        	return;
        }
        
        final MessageCategory messageCategory = messageCategoryDao.find( codeMessageCategory );
        
        // Button for deletePanel "Delete Category" has a popup behind it
        AreYouSurePanel deletePanel = createDeleteCategoryPanel(messageCategory, parent);
         
        add( deletePanel ); 
		// Form for add message button
        StatelessForm<Object> form = new StatelessForm<Object>("form");

        ResponseFormButton button = new ResponseFormButton("addnewmessage", "Add New Message", parent){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible(){
        		return parent.getActiveUser() != null;
        	}
			
			@Override
			public void onSubmit() {
				parent.addOrReplace(new ForumAddMessagePanel(parent));
		    }
        };
	    form.add(button);
		add(form);
		form.setVisible( getActiveUser() != null && getActiveUser().getCode() > 0 );

		MessageDao messageDao = new MessageDao();
		messageList = messageDao.getMessages( messageCategory );
	    	    
	    PageableListView <Message> listView = new PageableListView <Message>("messagelist", messageList, 30) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void populateItem(final ListItem<Message> listItem) {
				final ForumUserDao forumUserDao = new ForumUserDao();
				
				Message message 		= listItem.getModelObject();
				String 	displayName		= forumUserDao.find(message).getDisplayName();
				String 	messageDate 	= dateFormat.format(listItem.getModelObject().getMessageDate());
				String 	messageTime 	= timeFormat.format(listItem.getModelObject().getMessageDate());
				String 	description 	= message.getDescription();


                ClickableForumLabel messagedateLabel = new ClickableForumLabel( "messagedate", new Model<String>(messageDate));
                messagedateLabel.setParent(parent);
                messagedateLabel.setMessage(message);
                listItem.add(messagedateLabel);

                ClickableForumLabel messagetimeLabel = new ClickableForumLabel( "messagetime", new Model<String>(messageTime));
                messagetimeLabel.setParent(parent);
                messagetimeLabel.setMessage(message);
                listItem.add(messagetimeLabel);

                ClickableForumLabel userdisplaynameLabel = new ClickableForumLabel( "userdisplayname", new Model<String>(displayName));
                userdisplaynameLabel.setParent(parent);
                userdisplaynameLabel.setMessage(message);
                listItem.add(userdisplaynameLabel);
                
                ClickableForumLabel descriptionLabel = new ClickableForumLabel( "messagedescription", new Model<String>(description) );
                descriptionLabel.setParent(parent);
                descriptionLabel.setMessage(message);
                listItem.add(descriptionLabel);
			}
        };
		
        addOrReplace( listView ); 
        addOrReplace( new StatelessPagingNavigator( "navigator", parent.getPageParameters(), pageNumber, listView ) );       
	} 	

	
	// Creates a button to open popup "delete category yes/no"
	private AreYouSurePanel createDeleteCategoryPanel(final MessageCategory messageCategory, final ForumBasePage parent){
		return new AreYouSurePanel("deletePanel", "Delete Category", "Do you really want to delete this category?") { 
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible(){
				return isAdministrator(getActiveUser()) && messageCategory.getCode() != -1;
			}
			
			@Override
            protected void onConfirm(AjaxRequestTarget target) {
				// close current session first, to prevent session already managing the object
				DBHelper.closeSession();

				// delete category
				if( !DBHelper.deleteAndCommit(messageCategory)){
					parent.setErrorMessage("Failed to delete the category");

					// and return to forum home page
					setResponsePage(ForumBasePage.class);
				}
				
				// Synchronize necessary to prevent Cookie not having been set prior to rendering the new panel
				synchronized(this){
					CookieLogics.deleteCookie("codeMessage");
					CookieLogics.deleteCookie("codeMessageCategory");

					// and return to forum home page
					setResponsePage(ForumBasePage.class);
				}
            }

			@Override
			protected void onCancel(AjaxRequestTarget target) {
			}
		};
	}
}
