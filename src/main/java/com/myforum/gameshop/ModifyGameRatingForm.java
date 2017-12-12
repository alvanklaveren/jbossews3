package com.myforum.gameshop;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.myforum.application.DBHelper;
import com.myforum.application.ForumUtils;
import com.myforum.framework.AVKButton;
import com.myforum.gameshop.DDC.RatingUrlDDC;
import com.myforum.tables.ProductRating;

public class ModifyGameRatingForm extends Form<ProductRating> {
	private static final long serialVersionUID = 1L;
	
	public ModifyGameRatingForm(ProductRating productRating) {
		super("gameratingform");

		setDefaultModel( new CompoundPropertyModel<ProductRating>(productRating) );
		
        TextField<String> ratingField = new TextField<String>( "rating" );
        ratingField.setOutputMarkupId(true);

        add( ratingField );
		add( new RatingUrlDDC("gameratingurls", productRating, "ratingUrl", false /*isAutoSave*/ ).create() );
	
		Button saveButton   = createSaveButton();
		Button cancelButton = createCancelButton();

		add( saveButton );
		add( cancelButton );

	}

	
	/*
	 * Create a button that controls saving the product created in this form
	 */
	private Button createSaveButton(){
		final Form<ProductRating> form = this;
		
		Button saveEditButton = new AVKButton( "saveedit", "Save" ) {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			public void onSubmit() { 
				final TextField<String> ratingTF = (TextField<String>) form.get("rating");

				ProductRating productRating = form.getModelObject();				

				int rating = 0;
				try{
						rating = Integer.parseInt(ForumUtils.getInput(ratingTF));
				}catch(Exception e){
					String errorMessage = "Rating should be a number";
					getSession().setAttribute( "errormessage", errorMessage );

					reloadPage();
					return; // necessary, because otherwise the below lines will also be triggered.
				}
				
				productRating.setRating( rating );
							
				if ( !canSave(productRating) ){
					reloadPage();
					return; // necessary, because otherwise the below lines will also be triggered.
				}

				DBHelper.saveAndCommit(productRating); // cast up to product (so Hibernate recognizes the table)
				setResponsePage(GameShopPage.class);
				return;
			}
	    };   
	    saveEditButton.setDefaultFormProcessing( false );
	    return saveEditButton;
	}

	/*
	 * Creates a button that cancels the form and returns to the gameshop. For safety reasons, the transaction (for product) is rollbacked
	 */
	private Button createCancelButton(){
		Button saveEditButton = new AVKButton( "canceledit", "Cancel" ) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				DBHelper.rollback();
				setResponsePage(GameShopPage.class);
				return;
			}
	    };   
	    saveEditButton.setDefaultFormProcessing( false );
	    return saveEditButton;
	}

	/*
	 * Tests whether all the mandatory fields have been applied. If not, setErrorMessage()
	 */
	private boolean canSave(ProductRating productRating){
		String errorMessage = null;
		
		if(productRating.getRating() < 0)	{ errorMessage = "Please apply a rating"; }
		
		getSession().setAttribute( "errormessage", errorMessage );
		if(errorMessage != null){ return false; }
		
		return true;
	}
	
	private void reloadPage(){
		final Form<ProductRating> form = this;
		
		ProductRating productRating = form.getModelObject();
		
		int codeProduct = productRating.getProduct().getCode();
		PageParameters params = new PageParameters();
		params.set( "codeProduct", codeProduct);

		setResponsePage( ModifyGameRatingPage.class, params );
	}
}
