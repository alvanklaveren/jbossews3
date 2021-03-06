package com.myforum.forumpages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.Model;

import com.myforum.application.DBHelper;
import com.myforum.application.ForumUtils;
import com.myforum.base.ClickableForumLabel;
import com.myforum.dictionary.EText;
import com.myforum.framework.AVKButton;
import com.myforum.framework.AVKLabel;
import com.myforum.framework.StatelessPagingNavigator;
import com.myforum.security.CredentialLogics;
import com.myforum.tables.MessageCategory;
import com.myforum.tables.dao.MessageCategoryDao;

public class ForumHomePanel extends ForumBasePanel {
	private static final long serialVersionUID = 1L;

	private List<MessageCategory> 		messageCategories = Collections.synchronizedList(new ArrayList<MessageCategory>());
	private final MessageCategoryDao 	messageCategoryDao 	= new MessageCategoryDao();
	private long						pageNumber = 0;

	public ForumHomePanel(final ForumBasePage parent) {
		super(parent);
		
		pageNumber		= ForumUtils.getParmInt(parent.getPageParameters(),    "page", 0);

		messageCategories = messageCategoryDao.list();
		
		if( !CredentialLogics.isAdministrator(getActiveUser())){
			// Remove category -1, which is the category used on the homepage. This should only be available to the administrator.
			messageCategoryDao.removeCategory(messageCategories, -1);
		}

		add( new AVKLabel("category", "Category"));
		add( new AVKLabel("numberofmessages", EText.NUMBER_OF_MESSAGES));
		
		// Add messageCategoryListView of existing messageCategories
        PageableListView<MessageCategory> listView = new PageableListView<MessageCategory>("messagecategories", messageCategories, 30) {
			private static final long serialVersionUID = 1L;

			@Override
            public void populateItem(final ListItem<MessageCategory> listItem) {
				ClickableForumLabel categoryDescriptionLabel = new ClickableForumLabel( "description", new Model<String>(listItem.getModelObject().getDescription()) );
				categoryDescriptionLabel.setParent(parent);
				categoryDescriptionLabel.setMessageCategory(listItem.getModelObject());
				listItem.add( categoryDescriptionLabel );
				
				int messageCount = messageCategoryDao.getMessageCount( listItem.getModelObject() );
				listItem.add( new Label( "messagecount", messageCount ) );               
		        
				// Form for reply
				final StatelessForm<Object> renameCategoryForm = new StatelessForm<Object>( "renamecategoryform" );
				renameCategoryForm.setVisible(isAdministrator(getActiveUser()));
				listItem.add( renameCategoryForm );

				final TextField<String> categoryDescriptionTF = new TextField<String>( "categorydescription" );
				categoryDescriptionTF.setModel( new Model<String>( listItem.getModelObject().getDescription() ) );
				renameCategoryForm.add( categoryDescriptionTF );
				
			    Button renameButton = new AVKButton( "renamebutton", "Rename Category" ){
					private static final long serialVersionUID = 1L;

					public void onSubmit(){
						MessageCategory messageCategory = messageCategoryDao.find( listItem.getModelObject().getCode() );
						String newCategoryDescription = categoryDescriptionTF.getInput();
						if(newCategoryDescription.isEmpty()){ return; }
						
						messageCategory.setDescription( newCategoryDescription );
						
						if( DBHelper.saveAndCommit(messageCategory) == null ){
							parent.setErrorMessage("Failed to rename the category");
							parent.addOrReplace( new ForumHomePanel(parent) );
						}
						parent.addOrReplace( new ForumCategoryPanel(parent) );
			    	}
			    };

			    renameCategoryForm.addOrReplace( renameButton );
			}

        };
               
        addOrReplace( listView );
        addOrReplace( new StatelessPagingNavigator( "navigator", parent.getPageParameters(), pageNumber, listView ) );       
	}
	
}
