package com.myforum.forumpages;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;

import com.myforum.application.CookieLogics;
import com.myforum.application.StringLogics;
import com.myforum.framework.AVKButton;
import com.myforum.homepage.HomePage;
import com.myforum.tables.MessageCategory;
import com.myforum.tables.dao.MessageCategoryDao;

public class ForumAddMessagePanel extends ForumBasePanel {
	private static final long 		serialVersionUID 	= 1L;
	
	private ForumAddMessagePanel thisPanel;

	public ForumAddMessagePanel(final ForumBasePage parent){
		super(parent);
	
		thisPanel = this;
		
		final int codeMessageCategory = CookieLogics.getCookieInt("codeMessageCategory");
        if(codeMessageCategory == 0){
        	parent.setPanelErrorMessage("An error occurred: Could not find message category");
    		setResponsePage(ForumBasePage.class);
    		return;
		}
        if(getActiveUser() == null){
    		setResponsePage(HomePage.class);
    		return;       	
        }

        MessageCategory	messageCategory = new MessageCategoryDao().find( codeMessageCategory );
        
        final Label replyMessagePreview = new Label( "replymessagepreview", "" );
        replyMessagePreview.setEscapeModelStrings(false); 	// allow HTML
        replyMessagePreview.setEnabled(false); 				// user should not edit the preview
	
		// new message, i.e. not a reply to codeMessage
        CookieLogics.setCookieInt( "codeMessage", 0 );
	
		StatelessForm<Object> form = new ForumMessageForm( "newmessageform", getActiveUser(), parent );
		
		@SuppressWarnings("unchecked")
		final TextField<String> replydescriptionTF = (TextField<String>) form.get("replydescription");
		replydescriptionTF.add( new AjaxFormComponentUpdatingBehavior( "onkeyup" ) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// I am not entirely sure why this is necessary, but it seems to be the only way to store
				// the information in replyDescription in the wicket session
				replydescriptionTF.setDefaultModel( new Model<String>( replydescriptionTF.getInput() ) );
			}
	    });
		
		@SuppressWarnings("unchecked")
		final TextArea<String> replyMessageTA = (TextArea<String>) form.get("replymessage");
		replyMessageTA.add( new AjaxFormComponentUpdatingBehavior("onkeyup") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				replyMessagePreview.setDefaultModel( new Model<String>( replyMessageTA.getInput() ) );
			}
	    });
		
		Button previewButton = new AVKButton( "preview", "Preview Message" );
		previewButton.add( new AjaxEventBehavior( "onclick" ) {
			private static final long serialVersionUID = 1L;
			@Override
	        protected void onEvent(AjaxRequestTarget target) {
				String replyMessage = "";

				if (replyMessageTA != null && replyMessageTA.getDefaultModel() != null && replyMessageTA.getDefaultModel().getObject() != null) {
				
					replyMessage = replyMessageTA.getDefaultModel().getObject().toString();
					if (replyMessage == null) {
						replyMessage= "";
					}
				} 
				
				replyMessage = StringLogics.prepareMessage( replyMessage );
				replyMessagePreview.setDefaultModel( new Model<String>( replyMessage ) );
        		target.add(thisPanel);
        		return;
			}
		});

		previewButton.setDefaultFormProcessing( false );

		
		addOrReplace(new Label("messagecategory", new Model<String>(messageCategory.getDescription())));
		addOrReplace(form);
		addOrReplace(previewButton);
		addOrReplace(replyMessagePreview);	
	}
		
}
