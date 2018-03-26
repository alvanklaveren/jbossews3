package com.myforum.gameshop.codetable;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.myforum.application.DBHelper;
import com.myforum.application.ForumUtils;
import com.myforum.application.StringLogics;
import com.myforum.base.BasePage;
import com.myforum.base.menu.EMenuItem;
import com.myforum.tables.ProductType;
import com.myforum.tables.dao.ProductTypeDao;


public class ModifyProductTypePage extends BasePage {
	private static final long serialVersionUID = 1L;

	private final TextArea<String> editDescription;
	
	public ModifyProductTypePage(PageParameters params) {
		super(EMenuItem.DUMMY);
		
		int code = ForumUtils.getParmInt(params, "code", -1);
		
		final ProductType productType = new ProductTypeDao().find(code);
		
		// Form for product info
		final Form<ProductType> productTypeForm = new Form<ProductType>( "codetableform" ) {
			private static final long serialVersionUID = 1L;
		};
		
		editDescription = new TextArea<String>( "editdescription" );
		editDescription.setModel( new PropertyModel<String>(productType, "description") );
		editDescription.setOutputMarkupId(true);
		productTypeForm.add(editDescription);
		
		editDescription.add(new AjaxEventBehavior("onchange"){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onEvent(AjaxRequestTarget target) {
				productType.setDescription(editDescription.getInput());
			}
			
		});

		productTypeForm.add(createSaveEditButton(productTypeForm, productType));
		productTypeForm.add(createCancelButton(productTypeForm, productType));
		
		add(new Label("codetable", Model.of("Modify Product Type")));		
		add(productTypeForm);
	}

	/*
	 * Create a button that controls saving the product created in this form
	 */
	private Button createSaveEditButton(final Form<ProductType> form, final ProductType productType){
		Button saveEditButton = new Button( "saveedit" );
		saveEditButton.add( new AjaxEventBehavior("onclick"){
			private static final long serialVersionUID = 1L;


			@Override
			protected void onEvent(AjaxRequestTarget target) {			
				if (!canSave(productType)){
					//setResponsePage(AddProductTypePage.class, toPageParameters(productType));
					return; // necessary, because otherwise the below lines will also be triggered.
				}

				DBHelper.saveAndCommit(productType);

				// reset the error message
				setErrorMessage( "" );
				setResponsePage(CTProductTypePage.class);			
				return;
			}
	    });   
	    saveEditButton.setDefaultFormProcessing( false );
	    return saveEditButton;
	}

	/*
	 * Creates a button that cancels the form and returns to the gameshop. For safety reasons, the transaction (for product) is rollbacked
	 */
	private Button createCancelButton(final Form<ProductType> form, final ProductType productType){
		Button cancelEditButton = new Button( "canceledit" );
		cancelEditButton.add( new AjaxEventBehavior("onclick"){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onEvent(AjaxRequestTarget target) {
				DBHelper.rollback();
				setResponsePage(CTProductTypePage.class);			
				return;
			}
	    });   
	    cancelEditButton.setDefaultFormProcessing( false );
	    return cancelEditButton;
	}

	/*
	 * Tests whether all the mandatory fields have been applied. If not, setErrorMessage()
	 */
	private boolean canSave(ProductType productType){
		String description = productType.getDescription();
		if (StringLogics.isEmpty(description)){
			setErrorMessage( "Description is empty" );
			return false;
		}
		
		if( new ProductTypeDao().findByDescription( description ) != null ){
			setErrorMessage( "ProductType '" + description + "' already exists in the database" );
			return false;
		};
			
		return true;
	}
}

