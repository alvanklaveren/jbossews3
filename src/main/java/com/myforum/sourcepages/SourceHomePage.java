package com.myforum.sourcepages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.Model;

import com.myforum.application.DBHelper;
import com.myforum.base.AVKPage;
import com.myforum.base.menu.EMenuItem;
import com.myforum.framework.StatefulPagingNavigator;
import com.myforum.security.CredentialLogics;
import com.myforum.tables.SourceCategory;
import com.myforum.tables.dao.SourceCategoryDao;

public class SourceHomePage extends AVKPage {

	private static final long 			serialVersionUID 	= 1L;
	private List<SourceCategory> 		sourceCategoryList  = Collections.synchronizedList( new ArrayList<SourceCategory>() );
	private final SourceCategoryDao 	sourceCategoryDao 	= new SourceCategoryDao();
	
	public SourceHomePage() {
		super(EMenuItem.Sources);
		sourceCategoryList = sourceCategoryDao.list();     
	
		// Add SourceCategoryListView of existing messageCategories
        PageableListView<SourceCategory> listView = new PageableListView<SourceCategory>("sourcecategories", sourceCategoryList, 30) {
			private static final long serialVersionUID = 1L;

			@Override
            public void populateItem(final ListItem<SourceCategory> listItem) {
				Label categoryDescriptionLabel = new Label( "description", listItem.getModelObject().getDescription() );
				listItem.add( categoryDescriptionLabel );

				categoryDescriptionLabel.add( new AjaxEventBehavior( "onclick") {
					private static final long serialVersionUID = 1L;
					@Override
                    protected void onEvent(AjaxRequestTarget target) {
                        getSession().setAttribute("codeSourceCategory", listItem.getModelObject().getCode() );
                        setResponsePage( new SourceCategoryPage() );
                        return;
                    }
                });			
				               
    			// Form for reply
    			final StatelessForm<Object> renameCategoryForm = new StatelessForm<Object>( "renamecategoryform" ) {
    				private static final long serialVersionUID = 1L;
    				
    				@Override
    				public boolean isVisible(){
    					return CredentialLogics.isAdministrator(getActiveUser());
    				}
    			};   			
    			listItem.add( renameCategoryForm );

    			final TextField<String> categoryDescriptionTF = new TextField<String>( "categorydescription" );
    			categoryDescriptionTF.setModel( new Model<String>( listItem.getModelObject().getDescription() ) );
    			renameCategoryForm.add( categoryDescriptionTF );
    			
    		    Button renameButton = new Button( "renamebutton" ){
					private static final long serialVersionUID = 1L;

					public void onSubmit(){
    					SourceCategory sourceCategory = sourceCategoryDao.find( listItem.getModelObject().getCode() );
    					sourceCategory.setDescription( categoryDescriptionTF.getInput() );
    					
    					if( sourceCategoryDao.update( sourceCategory ) ){
    						DBHelper.getTransaction().commit();
    					}else{
    						DBHelper.getTransaction().rollback();
    					}
                        setResponsePage( getWebPage() );    
                        return;
    		    	}
    		    };

    		    renameCategoryForm.add( renameButton );
			}

        };

        addOrReplace( listView );
        addOrReplace( new StatefulPagingNavigator( "navigator", listView ) );
              
	    resetErrorMessage();
	}
	
}
