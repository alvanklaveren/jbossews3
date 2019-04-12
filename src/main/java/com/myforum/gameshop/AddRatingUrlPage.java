package com.myforum.gameshop;

import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.PropertyModel;

import com.myforum.application.DBHelper;
import com.myforum.application.StringLogics;
import com.myforum.base.BasePage;
import com.myforum.base.dictionary.EText;
import com.myforum.framework.ModalPage;
import com.myforum.tables.RatingUrl;
import com.myforum.tables.dao.RatingUrlDao;

public class AddRatingUrlPage extends ModalPage {
	private static final long serialVersionUID = 1L;

	private final TextArea<String> editDescription;
	
	public AddRatingUrlPage(final PageReference modalWindowPageRef, BasePage originPage) {
		super(modalWindowPageRef, originPage);
		
		final RatingUrl ratingUrl = new RatingUrl();
		
		// Form for product info
		final Form<RatingUrl> ratingUrlForm = new Form<RatingUrl>( "ratingurlform" ) {
			private static final long serialVersionUID = 1L;
		};
		
		editDescription = new TextArea<String>( "editdescription" );
		editDescription.setModel( new PropertyModel<String>(ratingUrl, "url") );
		editDescription.setOutputMarkupId(true);
		ratingUrlForm.add(editDescription);
		
		editDescription.add(new AjaxEventBehavior("onchange"){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onEvent(AjaxRequestTarget target) {
				ratingUrl.setUrl(editDescription.getInput());
			}
			
		});

		ratingUrlForm.add(createSaveEditButton(ratingUrlForm, ratingUrl));
		ratingUrlForm.add(createCancelButton(ratingUrlForm, ratingUrl));
		
		add(ratingUrlForm);
	}

	/*
	 * Create a button that controls saving the product created in this form
	 */
	private Button createSaveEditButton(final Form<RatingUrl> form, final RatingUrl ratingUrl){
		Button saveEditButton = new Button( "saveedit" );
		saveEditButton.add( new AjaxEventBehavior("onclick"){
			private static final long serialVersionUID = 1L;


			@Override
			protected void onEvent(AjaxRequestTarget target) {			
				if (canSave(ratingUrl)){
					DBHelper.saveAndCommit(ratingUrl);

					// reset the error message
					originPage.setErrorMessage( "" );
				}else {
					DBHelper.rollback();
				}
				
				parent.close(target);
				
				return;
			}
	    });   
	    saveEditButton.setDefaultFormProcessing( false );
	    return saveEditButton;
	}

	/*
	 * Creates a button that cancels the form and returns to the gameshop. For safety reasons, the transaction (for product) is rollbacked
	 */
	private Button createCancelButton(final Form<RatingUrl> form, final RatingUrl ratingUrl){
		Button cancelEditButton = new Button( "canceledit" );
		cancelEditButton.add( new AjaxEventBehavior("onclick"){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onEvent(AjaxRequestTarget target) {
				DBHelper.rollback();
				parent.close(target);
				return;
			}
	    });   
	    cancelEditButton.setDefaultFormProcessing( false );
	    return cancelEditButton;
	}

	/*
	 * Tests whether all the mandatory fields have been applied. If not, setErrorMessage()
	 */
	private boolean canSave(RatingUrl ratingUrl){
		String url = ratingUrl.getUrl();
		if (StringLogics.isEmpty(url)){
			originPage.setErrorMessage( EText.PLEASE_ADD_URL );
			return false;
		}
		
		if( new RatingUrlDao().findKey( url ) > 0 ){
			originPage.setErrorMessage( "Rating URL '" + url + "' already exists in the database" );
			return false;
		};
			
		return true;
	}
	
	@Override
	protected String getTitle(){
		return "";
	}

}
