package com.myforum.sourcepages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.myforum.application.DBHelper;
import com.myforum.application.StringLogics;
import com.myforum.base.BasePage;
import com.myforum.base.menu.EMenuItem;
import com.myforum.framework.AreYouSurePanel;
import com.myforum.security.CredentialLogics;
import com.myforum.tables.SourceCategory;
import com.myforum.tables.SourceSubject;
import com.myforum.tables.SourceText;
import com.myforum.tables.SourceType;
import com.myforum.tables.dao.SourceCategoryDao;
import com.myforum.tables.dao.SourceSubjectDao;
import com.myforum.tables.dao.SourceTextDao;
import com.myforum.tables.dao.SourceTypeDao;

public class SourceCategoryPage extends BasePage{

	private static final long 			serialVersionUID = 1L;

	private final SourceCategoryDao 	sourceCategoryDao 	= new SourceCategoryDao();
	private List<SourceSubject> 		subjectList 		= Collections.synchronizedList( new ArrayList<SourceSubject>() );
	private boolean 					isAdministrator 	= CredentialLogics.isAdministrator(getActiveUser());
	
	public SourceCategoryPage(){
		super(EMenuItem.Sources);

        if( getSession().getAttribute( "codeSourceCategory" ) == null ){
            setResponsePage( new SourceHomePage() );   
            return;
        }	
        final int codeSourceCategory = (Integer) getSession().getAttribute( "codeSourceCategory" );
        
    	final SourceCategory	sourceCategory 	= sourceCategoryDao.find( codeSourceCategory );

		AreYouSurePanel deletePanel = new AreYouSurePanel("deletePanel", "Delete Category", "Do you really want to delete this category?") { 
			private static final long serialVersionUID = 1L;

			@Override
            protected void onConfirm(AjaxRequestTarget target) {
				// delete message
				if( sourceCategoryDao.remove( sourceCategory ) ){
					DBHelper.getTransaction().commit();
				}else{
					DBHelper.getTransaction().rollback();
				}
				
				// and return to sourcehomepage since category is deleted
                setResponsePage( new SourceHomePage() );
                return;
            }
			
			@Override
			public boolean isVisible(){
				return isAdministrator;
			}
 
            @Override
            protected void onCancel(AjaxRequestTarget target) { }
 
        };
        
       	add( deletePanel ); 

    	// Form for adding new topic
        StatelessForm<Object> newTopicForm = new StatelessForm<Object>( "newtopicform" ) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isVisible(){
				return isAdministrator;
			}
		};

	    final SourceText newSourceText = new SourceText();
	    final TextField<String> topicTF = new TextField<String>( "topic" );

	    Button addNewTopicButton = new Button("addnewtopic") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				if( newSourceText.getSourceType() == null || StringLogics.isEmpty( topicTF.getInput() ) ){
					setErrorMessage( "Select a source type and choose a topic" );
					setResponsePage( new SourceCategoryPage() );
					return;
				}
				
				SourceSubject newSourceSubject = new SourceSubject();
				newSourceSubject.setSourceCategory( sourceCategory );
				newSourceSubject.setDescription( topicTF.getInput() );
				newSourceSubject.setSortOrder( 999 ); // default set to 999. When you get the PK, replace it 

				if( new SourceSubjectDao().update( newSourceSubject ) ){
					// do not commit yet. Wait for SourceText to save properly
				}else{
					DBHelper.getTransaction().rollback();
					setErrorMessage( "Saving new subject failed" );
					setResponsePage( new SourceCategoryPage() );
					return;
				}

				newSourceText.setSourceSubject( newSourceSubject );
				newSourceText.setHTMLDocument( "Under Construction for " + newSourceSubject.getDescription() );

				if( new SourceTextDao().update( newSourceText ) ){
					DBHelper.getTransaction().commit();
				}else{
					DBHelper.getTransaction().rollback();
					setErrorMessage( "Saving new subject/text failed" );
					setResponsePage( new SourceCategoryPage() );
					return;
				}

				newSourceSubject.setSortOrder( newSourceSubject.getCode() );
				if( new SourceSubjectDao().update( newSourceSubject ) ){
					DBHelper.getTransaction().commit();
				}else{
					DBHelper.getTransaction().rollback();
					setErrorMessage( "Changing sortorder in SourceSubject failed" );
					setResponsePage( new SourceCategoryPage() );
					return;
				}
				
				
				setResponsePage( new SourceCategoryPage() );
				return;
	        }
			
			@Override
			public boolean isVisible(){
				return isAdministrator;
			}
	    };
	    addNewTopicButton.setDefaultFormProcessing( false );
	        
	    List<SourceType> listSourceTypes = new SourceTypeDao().list();		
	   
		DropDownChoice<SourceType> listSourceTypeDDC =  
				new DropDownChoice<SourceType>( "sourcetypes", new PropertyModel<SourceType>( newSourceText, "sourceType" ), listSourceTypes ){
					private static final long serialVersionUID = 1L;
					
					protected boolean wantOnSelectionChangedNotifications(){
						return true;
					}
					
					@Override
					protected void onSelectionChanged( final SourceType newSourceType ){
	                	// Because we want to update the value immediately and don't want
	                	// to wait until the page renders again, force an Ajax onUpdate event
						newSourceText.setSourceType( newSourceType );
	    				getSession().setAttribute("ContentHasChanged", true );
					}
		
		};

		// Need a choice renderer to be able to set the selected item in the list to the current forumuser's classification 
		listSourceTypeDDC.setChoiceRenderer( new IChoiceRenderer<SourceType>() {
		        private static final long serialVersionUID = 1L;

				@Override
				public Object getDisplayValue( SourceType sourceType ) {	
					return sourceType.getDescription();
				}

				@Override
				public String getIdValue( SourceType sourceType, int index ) {
					return String.valueOf( sourceType.getCode() );
				}
								
            });	

		SourceSubjectDao sourceSubjectDao = new SourceSubjectDao();
		subjectList = sourceSubjectDao.list( sourceCategory );

	    PageableListView <SourceSubject> listView = new PageableListView <SourceSubject>( "subjectlist", subjectList, 30 ) {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(final ListItem<SourceSubject> listItem) {
				final SourceSubject sourceSubject 	= listItem.getModelObject();
				String 				description 	= sourceSubject.getDescription();

				Label descriptionLabel = new Label( "description", description );
                descriptionLabel.add( new AjaxEventBehavior( "onclick" ) {
					private static final long serialVersionUID = 1L;
					@Override
                    protected void onEvent(AjaxRequestTarget target) {
						SourceSubject sourceSubject = listItem.getModelObject();

                        getSession().setAttribute( "codeSourceSubject", sourceSubject.getCode() );

                        setResponsePage( new SourceSubjectPage() );
                        return;
                    }
                });
                
    			// Form for reply
    			final StatelessForm<Object> renameCategoryForm = new StatelessForm<Object>( "renamecategoryform" ) {
    				private static final long serialVersionUID = 1L;
    				
    				@Override
    				public boolean isVisible(){
    					return isAdministrator;
    				}
    			};   			
				
    			final TextField<String> subjectDescriptionTF = new TextField<String>( "subjectdescription" );
    			subjectDescriptionTF.setModel( new Model<String>( listItem.getModelObject().getDescription() ) );
    			
    		    Button renameButton = new Button( "renamebutton" ){
					private static final long serialVersionUID = 1L;

					public void onSubmit(){
						SourceSubjectDao sourceSubjectDao = new SourceSubjectDao();
						SourceSubject sourceSubject = sourceSubjectDao.find( listItem.getModelObject().getCode() );
    					sourceSubject.setDescription( subjectDescriptionTF.getInput() );
    					
    					if( sourceSubjectDao.update( sourceSubject ) ){
    						DBHelper.getTransaction().commit();
    					}else{
    						DBHelper.getTransaction().rollback();
    					}
                        setResponsePage( getWebPage() );   
                        return;
    		    	}
    		    };

    		    
    			// Form for reply
    			final StatelessForm<Object> addSourceTypeForm = new StatelessForm<Object>( "addsourcetypeform" ) {
    				private static final long serialVersionUID = 1L;
    				
    				@Override
    				public boolean isVisible(){
    					return isAdministrator;
    				}
    			};   			

    		    List<SourceType> 	listSourceTypes 	= new SourceTypeDao().list();
    		    final SourceText	newTopicSourceText	= new SourceText();
    		    
    			final DropDownChoice<SourceType> listSourceTypeDDC =  
    					new DropDownChoice<SourceType>( "newsourcetypes", new PropertyModel<SourceType>( newTopicSourceText, "sourceType" ), listSourceTypes ){
    						private static final long serialVersionUID = 1L;
    						
    						protected boolean wantOnSelectionChangedNotifications(){
    							return true;
    						}
    						
    						@Override
    						protected void onSelectionChanged( final SourceType newSourceType ){
    							newTopicSourceText.setSourceType(newSourceType);
    		    				getSession().setAttribute("ContentHasChanged", true );
    						}
    			
    			};

    			// Need a choice renderer to be able to set the selected item in the list to the current forumuser's classification 
    			listSourceTypeDDC.setChoiceRenderer( new IChoiceRenderer<SourceType>() {
    			        private static final long serialVersionUID = 1L;

    					@Override
    					public Object getDisplayValue( SourceType sourceType ) {	
    						return sourceType.getDescription();
    					}

    					@Override
    					public String getIdValue( SourceType sourceType, int index ) {
    						return String.valueOf( sourceType.getCode() );
    					}
    									
    	            });	

    		    Button addNewSourceType = new Button( "addnewsourcetype" ){
    				private static final long serialVersionUID = 1L;

    				@Override
    				public void onSubmit() {
    					if( StringLogics.isEmpty( listSourceTypeDDC.getValue() ) ){
    					//if( newSourceText.getSourceType() == null ){
    						setErrorMessage( "Select a source type" );
    						setResponsePage( new SourceCategoryPage() );
    						return;
    					}
    					    					
    					int codeSourceType = 0;
    					try{ 
    						codeSourceType = Integer.parseInt( listSourceTypeDDC.getValue() );
    					 } catch (NumberFormatException nfe) {
    						 // tough luck. It is not a number (this should never happen by the way.
    						setErrorMessage( "Assertion Failure: Key value in source type dropdown is NaN" );
     						setResponsePage( new SourceCategoryPage() );
     						return;   						 
    					 }
    					
    					if( new SourceTextDao().find( sourceSubject.getCode(), codeSourceType ) != null ){
    						setErrorMessage( "Selected source type for this topic is already available" );
    						setResponsePage( new SourceCategoryPage() );
    						return;
    					}

    					newSourceText.setSourceSubject( sourceSubject );
    					newSourceText.setSourceType( new SourceTypeDao().find(codeSourceType) );
    					newSourceText.setHTMLDocument( "Under Construction for " + sourceSubject.getDescription() );

    					if( new SourceTextDao().update( newSourceText ) ){
    						DBHelper.getTransaction().commit();
    					}else{
    						DBHelper.getTransaction().rollback();
    						setErrorMessage( "Saving new subject/text failed" );
    						setResponsePage( new SourceCategoryPage() );
    						return;
    					}
    					
						setResponsePage( new SourceCategoryPage() );
						return;
    		        }
    				
    				@Override
    				public boolean isVisible(){
    					return isAdministrator;
    				}
    		    };
    		    addNewSourceType.setDefaultFormProcessing( false );

				listItem.add( descriptionLabel );

				renameCategoryForm.add( subjectDescriptionTF );
    		    renameCategoryForm.add( renameButton );
    			listItem.add( renameCategoryForm );

    			addSourceTypeForm.add( listSourceTypeDDC );
    			addSourceTypeForm.add( addNewSourceType );
    			listItem.add( addSourceTypeForm );

    			
			}
        };
        listView.setReuseItems( true );
        
        Label backLabel = new Label( "back", "<<< Back <<<" );
        backLabel.add( new AjaxEventBehavior("onclick") {
			private static final long 	serialVersionUID 		= 1L;       			
			
			@Override
	        protected void onEvent(AjaxRequestTarget target) {
				setResponsePage( new SourceHomePage() );
				return;
	        }
        } );
        
	    add( backLabel );
        add( new Label( "errormessage", getSession().getAttribute( "errormessage" ) ) );

	    newTopicForm.add( addNewTopicButton );
	    newTopicForm.add( topicTF );
	    newTopicForm.add( listSourceTypeDDC );
	    add( newTopicForm );

        add( listView ); 
        add( new PagingNavigator("navigator", listView) );
        
	    resetErrorMessage();
        
	} 	
}
