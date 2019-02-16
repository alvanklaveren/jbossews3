package com.myforum.gameshop;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;

import com.myforum.application.DBHelper;
import com.myforum.application.ForumUtils;
import com.myforum.application.StringLogics;
import com.myforum.framework.AVKButton;
import com.myforum.gameshop.DDC.CompanyDDC;
import com.myforum.gameshop.DDC.GameConsoleDDC;
import com.myforum.gameshop.DDC.ProductTypeDDC;
import com.myforum.models.AddProduct;
import com.myforum.tables.Product;

public class AddGameForm extends Form<AddProduct> {
	private static final long serialVersionUID = 1L;
	
	public AddGameForm(AddProduct newProduct) {
		super("newgameform");

		setModel( new CompoundPropertyModel<AddProduct>(newProduct) );
		
        TextField<String> editName = new TextField<String>( "editName" );
        editName.setOutputMarkupId(true);

        TextArea<String> editDescription = new TextArea<String>( "editDescription" );
		editDescription.setOutputMarkupId(true);

		TextField<String> editYear = new TextField<String>( "editYear" );
        editYear.setOutputMarkupId(true);

		Button saveButton   = createSaveButton();
		Button cancelButton = createCancelButton();
		
        add( editName );
		add( editDescription );
		add( editYear );
		add( new CompanyDDC(	"companies", 	newProduct, "company", 	   false /*isAutoSave*/ ).create() );
		add( new ProductTypeDDC("producttypes", newProduct, "productType", false /*isAutoSave*/ ).create() );
		add( new GameConsoleDDC("gameconsoles", newProduct, "gameConsole", false /*isAutoSave*/ ).create() );
		add( saveButton );
		add( cancelButton );
	}

	/*
	 * Create a button that controls saving the product created in this form
	 */
	private Button createSaveButton(){
		final Form<AddProduct> form = this;
		
		Button saveEditButton = new AVKButton( "saveedit", "Save" ) {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			public void onSubmit() { 
				final TextField<String> editNameTF	 	 = (TextField<String>) form.get("editName");
				final TextField<String> editYearTF	 	 = (TextField<String>) form.get("editYear");
				final TextArea<String> editDescriptionTF = (TextArea<String>)  form.get("editDescription");

				AddProduct newProduct = form.getModelObject();				

				newProduct.setName( ForumUtils.getInput(editNameTF) );
				newProduct.setDescription( ForumUtils.getInput(editDescriptionTF) );

				int year = 0;
				try {
					year = Integer.parseInt(ForumUtils.getInput(editYearTF));
				} catch (NumberFormatException e) {
					// too bad. Stick to zero
				}
				newProduct.setYear( year );
				
				if ( !canSave(newProduct) ){
					setResponsePage(AddGamePage.class, newProduct.toPageParameters());
					return; // necessary, because otherwise the below lines will also be triggered.
				}

				DBHelper.saveAndCommit(newProduct.toProduct()); // cast up to product (so Hibernate recognizes the table)
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
	private boolean canSave(Product product){
		String errorMessage = null;
		
		if(StringLogics.isEmpty(product.getName()))				{ errorMessage = "Name is empty"; }
		else if(StringLogics.isEmpty(product.getDescription())) { errorMessage = "Description is empty"; }
		else if(product.getGameConsole() == null)			  	{ errorMessage = "Please select a game console"; }
		else if(product.getProductType() == null)				{ errorMessage = "Please select a product type"; }
		else if(product.getCompany() == null)					{ errorMessage = "Please select a company"; }
		
		getSession().setAttribute( "errormessage", errorMessage );
		if(errorMessage != null){ return false; }
		
		return true;
	}
}
