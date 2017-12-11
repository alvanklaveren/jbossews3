package com.myforum.forumpages;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.AbstractTextComponent;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Bytes;

import com.myforum.application.AllConstants;
import com.myforum.application.DBHelper;
import com.myforum.application.ForumUtils;
import com.myforum.application.IoLogics;
import com.myforum.application.StringLogics;
import com.myforum.base.BasePage;
import com.myforum.base.ForumLogics;
import com.myforum.framework.ErrorLabel;
import com.myforum.framework.ResponseButton;
import com.myforum.security.CredentialLogics;
import com.myforum.tables.Classification;
import com.myforum.tables.ForumUser;
import com.myforum.tables.dao.ForumUserDao;

public class UserModifyAccountPage extends BasePage {
	private static final long 				serialVersionUID 	= 1L;
	private final 		 ForumUserDao		forumUserDao 		= new ForumUserDao(); 
	private static 		 Classification 	currentClassification;
	private 			 int		 		codeForumUser;
	
	public UserModifyAccountPage(PageParameters params) {
		getSession().setAttribute("ContentHasChanged", false );

        final ForumUser forumUser = getForumUserFromParameters(params);

		currentClassification = forumUser.getClassification();

		// Form for user account
		final Form<ForumUser> form = new Form<ForumUser>( "useraccountform" ) {
			private static final long serialVersionUID = 1L;
		};

		final Label usernameLabel 							= new Label( "username", new PropertyModel<String>( forumUser, "username" ) );
		final TextField<String> displayNameTF	 			= new TextField<String>( "displayname", new PropertyModel<String>( forumUser, "displayName") );
		final PasswordTextField oldPasswordTF 				= new PasswordTextField( "oldpassword", new Model<String>() );
		final PasswordTextField passwordTF 					= new PasswordTextField( "password", new Model<String>() ); 
		final PasswordTextField retypePasswordTF 			= new PasswordTextField( "retypepassword", new Model<String>() ); 
        final TextField<String> emailAddressTF 				= new TextField<String>( "emailaddress", new PropertyModel<String>( forumUser, "emailAddress") );
        final FileUploadField avatarfileUF 					= new FileUploadField( "avatarfile" );	
		DropDownChoice<Classification> classificationsDDC 	= createClassificationDDC(forumUser);
		Button uploadButton 								= createUploadButton(form, forumUser);		
		ResponseButton cancelButton							= new ResponseButton("cancel", new Model<String>(translator.translate("Cancel")), ForumBasePage.class);
		Button applyChangesButton 							= createApplyChangesButton(form, forumUser);
   
    	form.add( usernameLabel );
    	form.add( displayNameTF );
    	form.add( oldPasswordTF );
		form.add( passwordTF );
		form.add( retypePasswordTF );
        form.add( avatarfileUF );
		form.add( emailAddressTF );
		form.add( ForumUtils.loadAvatar( forumUser, "avatarpicture" ) );
		form.add( classificationsDDC );
	    form.add( uploadButton );
	    form.add( cancelButton );
	    form.add( applyChangesButton );			

	    addOrReplace(new ErrorLabel());
		addOrReplace( form );
	}

	
	
	// Logics	
	
	private void contentChanged(){
		getSession().setAttribute("ContentHasChanged", true );
	}

	private boolean hasChanged(AbstractTextComponent<String> component, String oldValue){
		String newValue 	= component.getInput().trim();
		boolean hasChanged 	= !newValue.equals(oldValue.trim()); 
		if(hasChanged){
			contentChanged();
		}
		return hasChanged;
	}
	
	private boolean needsSaving(){
		return (Boolean) getSession().getAttribute("ContentHasChanged" );
	}
	
	private boolean isValidEmailAddress(String emailAddress){
		if( !StringLogics.isEmpty( emailAddress ) && !StringLogics.validateEmailAddress( emailAddress ) ){ 
			return false; 
		}
		return true;
	}

	private void checkCurrentPassword(ForumUser forumUser, String currentPassword){
		if( !CredentialLogics.validCredentials(forumUser.getUsername(), currentPassword) ){
			setErrorMessage( "Current password is incorrect" );
            setResponsePage( UserModifyAccountPage.class, getPageParameters() );
	    	return;
		}
	}
	
	private void checkNewPassword(String newPassword, String retypePassword){
		if( !StringLogics.isEmpty(newPassword) ){
			if( newPassword.equals(retypePassword) ){
				setErrorMessage( "New password is not equal to retyped password" );			
                setResponsePage( UserModifyAccountPage.class, getPageParameters() );
                return;
			}
		}
	}
	
	private ForumUser getForumUserFromParameters(PageParameters params){
		String urlEncoded = AllConstants.getCrypt().decryptUrlSafe(params.get("codeforumuser").toString());
		codeForumUser = Integer.parseInt(urlEncoded);
        
        // If no user is logged in, leave this page and go back to forum
		if( codeForumUser <= 0 ){
			setResponsePage( ForumBasePage.class );
			return null;
		}
		
        ForumUser forumUser = forumUserDao.find( codeForumUser );

        if( !isActive(forumUser) && !isAdministrator(forumUser) ){
			setErrorMessage("Not allowed to change selected user !");       	
			setResponsePage( ForumBasePage.class );
			return forumUser;
		}
        return forumUser;
	}
	
	@SuppressWarnings("unchecked")
	private void applyChanges(Form<ForumUser> form, ForumUser forumUser){	
		
		final TextField<String> displayNameTF	 	= (TextField<String>) form.get("displayname");
		final PasswordTextField oldPasswordTF 		= (PasswordTextField) form.get("oldpassword");
		final PasswordTextField passwordTF 			= (PasswordTextField) form.get("password"); 
		final PasswordTextField retypePasswordTF 	= (PasswordTextField) form.get("retypepassword"); 
        final TextField<String> emailAddressTF 		= (TextField<String>) form.get("emailaddress");

		String currentPassword 	= ForumUtils.getInput( oldPasswordTF ); 
		String newPassword 		= ForumUtils.getInput( passwordTF ); 
		String retypePassword	= ForumUtils.getInput( retypePasswordTF ); 
		String emailAddress		= ForumUtils.getInput( emailAddressTF ); 
		String displayName		= ForumUtils.getInput( displayNameTF ); 

		if(hasChanged(passwordTF,"")){
			checkCurrentPassword(forumUser, currentPassword);
			checkNewPassword(newPassword, retypePassword);
		}

		String currentDisplayName = forumUser.getDisplayName();
		if( currentDisplayName == null ) currentDisplayName = "";
		if( hasChanged(displayNameTF, currentDisplayName) ){ 
			forumUser.setDisplayName( displayName );		
		}
		
		String currentEmailAddress = forumUser.getEmailAddress();
		if( currentEmailAddress == null ) currentEmailAddress = "";
		if( hasChanged(emailAddressTF, currentEmailAddress) ){ 
			if( !isValidEmailAddress(emailAddress) ){
				setErrorMessage( "Please enter a valid emailaddress" );
				String 			urlEncoded = AllConstants.getCrypt().encryptUrlSafe(String.valueOf( codeForumUser ));
				PageParameters 	parameters = new PageParameters().set("codeforumuser", urlEncoded);
				setResponsePage( UserModifyAccountPage.class, parameters );
				return;	    			
			}
			forumUser.setEmailAddress(emailAddress);
		}
					
		if( needsSaving() ){				
			if( DBHelper.saveAndCommit(forumUser)){
				setErrorMessage( "Changes saved" );
				getSession().setAttribute("ContentHasChanged", false );
				setResponsePage(ForumBasePage.class);
				return;
			}else{
				setErrorMessage( "Failed to save changes" );
			}
		} else {
			setErrorMessage( "No changes were made");
		}
		
		String 			urlEncoded = AllConstants.getCrypt().encryptUrlSafe(String.valueOf( codeForumUser ));
		PageParameters 	parameters = new PageParameters().set("codeforumuser", urlEncoded);
        setResponsePage(UserModifyAccountPage.class, parameters);
        return;
	}

	private DropDownChoice<Classification> createClassificationDDC(final ForumUser forumUser){
		List<Classification> classifications = ForumLogics.getClassifications(currentClassification, CredentialLogics.isAdministrator(getActiveUser()) );
		DropDownChoice<Classification> classificationsDDC =  
		new DropDownChoice<Classification>( "classifications", new PropertyModel<Classification>( forumUser, "classification" ), classifications ){
			private static final long serialVersionUID = 1L;
			
			protected boolean wantOnSelectionChangedNotifications(){
				return true;
			}
			
			@Override
			protected void onSelectionChanged( final Classification newSelection ){
	        	// Because we want to update the value immediately and don't want to wait until the page renders again, force an Ajax onUpdate event
	        	forumUser.setClassification( newSelection );
	        	contentChanged();
			}
		};
		
		// Need a choice renderer to be able to set the selected item in the list to the current forumuser's classification 
		classificationsDDC.setChoiceRenderer( new IChoiceRenderer<Classification>() {
		private static final long serialVersionUID = 1L;
		
		@Override
		public Object getDisplayValue( Classification classification ) {	
			return classification.toString();
		}
		
		@Override
		public String getIdValue( Classification classification, int index ) {
			return String.valueOf( classification.getCode() );
		}
							
		});	
		classificationsDDC.setModelObject( currentClassification );
		return classificationsDDC;
	}

	
	private void uploadFile(Form<ForumUser> form, ForumUser forumUser){
        final FileUploadField avatarfileUF = (FileUploadField) form.get("avatarfile"); 

		if( StringLogics.isEmpty( avatarfileUF.getInput() ) ){
			return;
		}
		
		byte[] imageData = IoLogics.readImageFile( avatarfileUF, "" );
		if( imageData != null && Bytes.megabytes( 1 ).lessThan( imageData.length ) ){
				setErrorMessage( "File exceed maximum size of 1 MB" );
		}else{
			// the below checks the size of the imageData length "greater than zero", as in the past "equal to zero" has caused stack overflow.
			if( imageData != null && imageData.length > 0 ){ forumUser.setAvatar( imageData ); }
			contentChanged();
		}
	}
	
	private Button createUploadButton(final Form<ForumUser> form, final ForumUser forumUser){
		Button uploadButton = new Button( "uploadbutton" ){
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				uploadFile(form, forumUser);
			}
		};
		uploadButton.setDefaultFormProcessing( false );
		return uploadButton;
	}

	private Button createApplyChangesButton(final Form<ForumUser> form, final ForumUser forumUser){
		Button applyChangesButton = new Button( "applychanges", new Model<String>(translator.translate("Apply Changes")) ) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				applyChanges(form, forumUser);
			}
	    };   
	    applyChangesButton.setDefaultFormProcessing( false );
	    return applyChangesButton;
	}
	
}

  
