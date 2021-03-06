package com.myforum.forumpages;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.Model;

import com.myforum.application.CookieLogics;
import com.myforum.application.StringLogics;
import com.myforum.base.ForumLogics;
import com.myforum.dictionary.EText;
import com.myforum.forumpages.images.MessageImagePage;
import com.myforum.framework.AVKButton;
import com.myforum.framework.AVKLabel;
import com.myforum.framework.AnswerPopup;
import com.myforum.gameshop.ResponseFormModalButton;
import com.myforum.homepage.HomePage;
import com.myforum.tables.MessageImage;

public class ForumAddMessagePanel extends ForumBasePanel {
	private static final long 		serialVersionUID 	= 1L;
	
	private ForumAddMessagePanel thisPanel;

	private AnswerPopup modalResult;
	
	public ForumAddMessagePanel(final ForumBasePage parent){
		super(parent);
	
		thisPanel = this;
		modalResult = new AnswerPopup("");
				
		final int codeMessageCategory = CookieLogics.getCookieInt("codeMessageCategory");
        if(codeMessageCategory == 0){
        	parent.setErrorMessage("An error occurred: Could not find message category");
    		setResponsePage(ForumBasePage.class);
    		return;
		}
        if(getActiveUser() == null){
    		setResponsePage(HomePage.class);
    		return;       	
        }

        //MessageCategory	messageCategory = new MessageCategoryDao().find( codeMessageCategory );
        
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

        // Create the modal window.
        ModalWindow messageImageModal = createSelectImageModal(replyMessageTA);
		add(messageImageModal);

        final Form<String> selectForm = new Form<String>("selectform");
        selectForm.add( createFormModalButton( "selectimage", "Select Image", messageImageModal) );
        addOrReplace(selectForm);

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

	    add(createImageUploadForm(parent, replyMessageTA));
		
		addOrReplace(form);
		addOrReplace(previewButton);
		addOrReplace(replyMessagePreview);	
	}
	
	private ModalWindow createSelectImageModal(final TextArea<String> messageTA) {
        // Create the modal window.
        ModalWindow messageImageModal = new ModalWindow("modalMessageImagePage");
        final MessageImagePage messageImagePage = new MessageImagePage(messageImageModal, parent.getPageReference(), parent, modalResult);
    	messageImageModal.setResizable(true);
    	messageImageModal.setAutoSize(false);
    	messageImageModal.setInitialWidth(300);
    	messageImageModal.setInitialHeight(600);
      	messageImageModal.setContent(messageImagePage);
        messageImagePage.setParent(messageImageModal);
        
		messageImageModal.setWindowClosedCallback( new ModalWindow.WindowClosedCallback() {

			private static final long serialVersionUID = 1L;

			@Override
            public void onClose( AjaxRequestTarget target ) {
                if ( modalResult.getAnswer() != null ) {
    				String messageText = messageTA.getModelObject() != null ? messageTA.getModelObject() : "";
    				messageText += modalResult.getAnswer();  				
    				messageTA.setModelObject(messageText);
    				target.add(thisPanel);   				
                }
            }
        });
		
		return messageImageModal;
	}
	
	private Button createUploadButton(final FileUploadField imageFile, final ForumBasePage parent, final TextArea<String> replyMessageTA){
		Button uploadButton = new Button( "uploadbutton" ){
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				MessageImage messageImage = ForumLogics.uploadImage(imageFile);
				if(messageImage == null){
					parent.setPanelErrorMessage( EText.UPLOAD_FAILED );
					return;
				};	
				
				String messageText = replyMessageTA.getModelObject() != null ? replyMessageTA.getModelObject() : "";
				messageText += "[i:" + messageImage.getCode() +"]";
				replyMessageTA.setModel(new Model<String>(messageText));						
				return;
			}
		};
		uploadButton.setDefaultFormProcessing( false );
		uploadButton.setOutputMarkupId(true);
		return uploadButton;
	}

	/*
	 * Creates a from for uploading an image in a listItem
	 */
	private Form<MessageImage> createImageUploadForm(final ForumBasePage parent, final TextArea<String> replyMessageTA){
		// Form for messageImage
		final Form<MessageImage> uploadForm = new Form<MessageImage>( "uploadform" );
		uploadForm.setOutputMarkupId(true);
		
		final FileUploadField imageFile = new FileUploadField( "messageImage" );
		imageFile.setOutputMarkupId(true);
		uploadForm.add(imageFile);

		Button uploadButton = createUploadButton(imageFile, parent, replyMessageTA);
		uploadForm.add(uploadButton);

		uploadForm.setVisible(true);
		return uploadForm;
	}
		
	/*
	 * The below function creates form buttons, like 'Add Company', which will open a MODAL WINDOW when clicked. 
	 * @Arguments: id - name in html, like selectmessagebutton, but without the 'button' text, so we can reuse selectmessage for the label  
	 */
	private ResponseFormModalButton createFormModalButton( String id, String buttonText, ModalWindow modal ){
        ResponseFormModalButton formButton = new ResponseFormModalButton(id + "button", buttonText, modal, isAdministrator(getActiveUser()));

        final Label buttonLabel = new AVKLabel( id + "label", buttonText );
        formButton.add(buttonLabel);

        return formButton;
	}

}
