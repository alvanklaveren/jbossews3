package com.myforum.gameshop.codetable;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.myforum.application.DBHelper;
import com.myforum.application.ForumUtils;
import com.myforum.application.StringLogics;
import com.myforum.base.AVKPage;
import com.myforum.base.menu.EMenuItem;
import com.myforum.tables.GameConsole;
import com.myforum.tables.dao.GameConsoleDao;


public class ModifyGameConsolePage extends AVKPage {
	private static final long serialVersionUID = 1L;

	private final TextArea<String> editDescription;
	private final NumberTextField<Integer> editSortorder;
	
	public ModifyGameConsolePage(PageParameters params) {
		super(EMenuItem.DUMMY);
		
		int code = ForumUtils.getParmInt(params, "code", -1);
		
		final GameConsole gameConsole = new GameConsoleDao().find(code);
		
		// Form for product info
		final Form<GameConsole> gameConsoleForm = new Form<GameConsole>( "codetableform" ) {
			private static final long serialVersionUID = 1L;
		};
		
		editDescription = new TextArea<String>( "editdescription" );
		editDescription.setModel( new PropertyModel<String>( gameConsole, "description") );
		editDescription.setOutputMarkupId(true);
		gameConsoleForm.add(editDescription);
		
		editDescription.add(new AjaxEventBehavior("onchange"){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onEvent(AjaxRequestTarget target) {
				gameConsole.setDescription(editDescription.getInput());
			}
			
		});

		// <input type="number" class="form-control" id="editsortorder" wicket:id="editsortorder" placeholder="Sortorder">
		//editSortorder = new NumberTextField<Integer>( "sortorder" ).setMinimum(0).setMaximum(99);

		editSortorder = new NumberTextField<Integer>( "editsortorder" );
		editSortorder.setModel( new PropertyModel<Integer>( gameConsole, "sortorder") );
		editSortorder.setOutputMarkupId(true);
		gameConsoleForm.add(editSortorder);
		
		editSortorder.add(new AjaxEventBehavior("onchange"){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onEvent(AjaxRequestTarget target) {
				int sortorder = Integer.parseInt(editSortorder.getInput());
				gameConsole.setSortorder( sortorder );
			}
			
		});

		gameConsoleForm.add(createSaveEditButton(gameConsoleForm, gameConsole));
		gameConsoleForm.add(createCancelButton(gameConsoleForm, gameConsole));
		
		add(new Label("codetable", Model.of("Modify GameConsole")));		
		add(gameConsoleForm);
	}

	/*
	 * Create a button that controls saving the product created in this form
	 */
	private Button createSaveEditButton(final Form<GameConsole> form, final GameConsole gameConsole){
		Button saveEditButton = new Button( "saveedit" );
		saveEditButton.add( new AjaxEventBehavior("onclick"){
			private static final long serialVersionUID = 1L;


			@Override
			protected void onEvent(AjaxRequestTarget target) {			
				if (!canSave(gameConsole)){
					//setResponsePage(AddGameConsolePage.class, toPageParameters(gameConsole));
					return; // necessary, because otherwise the below lines will also be triggered.
				}

				DBHelper.saveAndCommit(gameConsole);

				// reset the error message
				setErrorMessage( "" );
				setResponsePage(CTGameConsolePage.class);			
				return;
			}
	    });   
	    saveEditButton.setDefaultFormProcessing( false );
	    return saveEditButton;
	}

	/*
	 * Creates a button that cancels the form and returns to the gameshop. For safety reasons, the transaction (for product) is rollbacked
	 */
	private Button createCancelButton(final Form<GameConsole> form, final GameConsole gameConsole){
		Button cancelEditButton = new Button( "canceledit" );
		cancelEditButton.add( new AjaxEventBehavior("onclick"){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onEvent(AjaxRequestTarget target) {
				DBHelper.rollback();
				setResponsePage(CTGameConsolePage.class);			
				return;
			}
	    });   
	    cancelEditButton.setDefaultFormProcessing( false );
	    return cancelEditButton;
	}

	/*
	 * Tests whether all the mandatory fields have been applied. If not, setErrorMessage()
	 */
	private boolean canSave(GameConsole gameConsole){
		String description = gameConsole.getDescription();
		if (StringLogics.isEmpty(description)){
			setErrorMessage( "Description is empty" );
			return false;
		}
		
		return true;
	}
}

