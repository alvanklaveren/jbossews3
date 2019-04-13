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
import com.myforum.tables.Company;
import com.myforum.tables.dao.CompanyDao;

public class AddCompanyPage extends ModalPage {
	private static final long serialVersionUID = 1L;

	private final TextArea<String> editDescription;
	
	public AddCompanyPage(final PageReference modalWindowPageRef, final BasePage originPage) {
		super(modalWindowPageRef, originPage);
		
		final Company company = new Company();
		
		// Form for product info
		final Form<Company> companyForm = new Form<Company>( "companyform" ) {
			private static final long serialVersionUID = 1L;
		};
		
		editDescription = new TextArea<String>( "editdescription" );
		editDescription.setModel( new PropertyModel<String>(company, "description") );
		editDescription.setOutputMarkupId(true);
		companyForm.add(editDescription);
		
		editDescription.add(new AjaxEventBehavior("onchange"){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onEvent(AjaxRequestTarget target) {
				company.setDescription(editDescription.getInput());
			}
			
		});

		companyForm.add(createSaveEditButton(companyForm, company));
		companyForm.add(createCancelButton(companyForm, company));
		
		add(companyForm);
	}

	/*
	 * Create a button that controls saving the product created in this form
	 */
	private Button createSaveEditButton(final Form<Company> form, final Company company){
		Button saveEditButton = new Button( "saveedit" );
		saveEditButton.add( new AjaxEventBehavior("onclick"){
			private static final long serialVersionUID = 1L;


			@Override
			protected void onEvent(AjaxRequestTarget target) {			
				if (canSave(company)){
					DBHelper.saveAndCommit(company);

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
	private Button createCancelButton(final Form<Company> form, final Company company){
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
	private boolean canSave(Company company){
		String description = company.getDescription();
		if (StringLogics.isEmpty(description)){
			originPage.setErrorMessage(EText.PLEASE_ADD_COMPANY);
			return false;
		}
		
		if( new CompanyDao().findByDescription(description) != null ){
			originPage.setErrorMessage(EText.COMPANY_ALREADY_EXISTS);
			return false;
		};
			
		return true;
	}
	
	@Override
	protected String getTitle(){
		return "";
	}

}
