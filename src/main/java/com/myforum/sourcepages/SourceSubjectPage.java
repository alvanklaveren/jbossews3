package com.myforum.sourcepages;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.time.Duration;

import com.myforum.application.DBHelper;
import com.myforum.application.IoLogics;
import com.myforum.application.StringLogics;
import com.myforum.base.BasePage;
import com.myforum.base.menu.EMenuItem;
import com.myforum.framework.AreYouSurePanel;
import com.myforum.security.CredentialLogics;
import com.myforum.tables.SourceSubject;
import com.myforum.tables.SourceText;
import com.myforum.tables.SourceType;
import com.myforum.tables.dao.SourceSubjectDao;
import com.myforum.tables.dao.SourceTextDao;
import com.myforum.tables.dao.SourceTypeDao;

public class SourceSubjectPage extends BasePage {

	private static final long 		serialVersionUID = 1L;

	private final SourceTextDao		sourceTextDao		= new SourceTextDao();
	private final SourceTypeDao 	sourceTypeDao		= new SourceTypeDao();
	private final SourceSubjectDao	sourceSubjectDao	= new SourceSubjectDao();
	private boolean 				isAdministrator 	= CredentialLogics.isAdministrator(getActiveUser());

	public SourceSubjectPage(){
		super(EMenuItem.Sources);
        if( getSession().getAttribute( "codeSourceSubject" ) == null ){
            setResponsePage( new SourceHomePage() );        
            return;
        }     

        int codeSourceType = 3; // Java as default
        if( getSession().getAttribute( "codeSourceType" ) != null ){
        	codeSourceType = (Integer) getSession().getAttribute( "codeSourceType" );
        }     
        
        final int			codeSourceSubject	= (Integer) getSession().getAttribute( "codeSourceSubject" );
        final SourceSubject	sourceSubject		= sourceSubjectDao.find( codeSourceSubject );	

        SourceText initSourceText;
        // try and get the sourcetext, based on the subject and current (session) sourcetype.
        if( sourceTextDao.find( codeSourceSubject, codeSourceType ) != null ){
        	initSourceText	= sourceTextDao.find( codeSourceSubject, codeSourceType );
        } else{
        	// if that doesn't exist, then get the first subject you can find that belongs to this sourcesubject
        	initSourceText = sourceTextDao.list( sourceSubject ).get(0);
        	// now this last one should not be able to occur, but if it did, then something seriously went wrong. RETURN !!
        	if( initSourceText == null ){
        		setResponsePage( new SourceCategoryPage() );
        		return;
            }
        	
        	SourceType sourceType = sourceTypeDao.find( initSourceText );
        	if( sourceType != null){
        		codeSourceType = sourceType.getCode();
        		getSession().setAttribute( "codeSourceType", codeSourceType );
        	}
        }
                
        final SourceText	sourceText			= sourceTextDao.find( codeSourceSubject, codeSourceType );
        List<SourceText> 	sourceTextList 		= sourceTextDao.list( sourceSubject );
		
		AreYouSurePanel deletePanel = new AreYouSurePanel("deletePanel", "Delete Sourcetext", "Do you really want to delete this source Text?") { 
			private static final long serialVersionUID = 1L;

			@Override
            protected void onConfirm(AjaxRequestTarget target) {
				// delete sourceText
				if( sourceTextDao.remove( sourceText ) ){
					// when you successfully deleted the sourceText, and it appeared to be the last one connected to Subject,
					// then delete subject as well !!
					if( sourceTextDao.list( sourceSubject ).size() <= 1 && !sourceSubjectDao.remove( sourceSubject ) ){
						DBHelper.getTransaction().rollback();
						setErrorMessage( "Removing sourceSubject failed!" );
		                setResponsePage( new SourceSubjectPage() );		
		                return;
					}
				}else{
					DBHelper.getTransaction().rollback();
					setErrorMessage( "Removing sourceText failed!" );
	                setResponsePage( new SourceSubjectPage() );
	                return;
				}

				DBHelper.getTransaction().commit();
                setResponsePage( new SourceCategoryPage() );	
                return;

            }

			@Override
			protected void onCancel(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean isVisible(){
				return CredentialLogics.isAdministrator(getActiveUser());
			}
		};

		// create a list of ITab objects used to add links to all the sourcetypes available to the subject
		ArrayList<SourceType> sourceTypeList = new ArrayList<SourceType>();
		
		for( SourceText aText : sourceTextList ){
			SourceType aType = sourceTypeDao.find( aText );
			sourceTypeList.add( aType );
		}
		
		Collections.sort( sourceTypeList );
		
		
        PropertyListView<SourceType> sourceTypeListView = null;
        sourceTypeListView = ( new PropertyListView<SourceType>( "sourcetypelist", sourceTypeList ) {
			private static final long serialVersionUID = 1L;
			
			private String seperator = "";

			@Override
            public void populateItem(final ListItem<SourceType> listItem) {
			
				final SourceType sourceType = listItem.getModelObject();
				Label sourceTypeDescription = new Label( "sourcetypedescription", seperator + sourceType.getDescription() );
				
				sourceTypeDescription.add( new AjaxEventBehavior("onclick") {
        			private static final long 	serialVersionUID 		= 1L;       			
        			private 			 int 	sessionCodeSourceType 	= sourceType.getCode();
        			
        			@Override
        	        protected void onEvent(AjaxRequestTarget target) {
        	        	getSession().setAttribute( "codeSourceType", sessionCodeSourceType );
        				setResponsePage( new SourceSubjectPage() );
        				return;
        	        }
                } );

                listItem.add( sourceTypeDescription );
                seperator = "   ";

			}
        });   
        
        // setReuseItems( true ) is very important. Each time a page renders, the listview is rebuild with new IDs, making it impossible 
        // to address the items for, say, visibility. Mind that anything added to this listview's model, will be positioned at the END
        // of the list.
        sourceTypeListView.setReuseItems( true ); 
	
        StatelessForm<Object> htmlForm = new StatelessForm<Object>( "htmlform" ) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isVisible(){
				return isAdministrator;
			}
		};

        final FileUploadField importHtmlFileNameUF = new FileUploadField( "importhtmlfilename" ); 
		        
    	Button importHtmlButton = new Button( "importhtmlbutton" ) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				String fileContent = IoLogics.readTextFile( importHtmlFileNameUF );	
				if( fileContent.length() > 0 ){ 				
					sourceText.setHTMLDocument( fileContent.toString() );
			
					if( sourceTextDao.update( sourceText ) ){
						DBHelper.getTransaction().commit();
					}else{
						DBHelper.getTransaction().rollback();
						setErrorMessage( "Saving HTML failed" );
					}
				}
			
				setResponsePage( new SourceSubjectPage() );
				return;
	        }
	    };
		importHtmlButton.setDefaultFormProcessing( false );
		
		DownloadLink exportHtmlLink = new DownloadLink( "exporthtmllink", new AbstractReadOnlyModel<File>(){
	    	        private static final long serialVersionUID = 1L;

	    	        @Override
	    	        public File getObject()
	    	        {
	    	            File tempFile;
	    	            try
	    	            {
	    	            	String fileName = sourceSubject.getDescription() + "_" + sourceSubject.getCode() + "_" + sourceText.getSourceType().getCode(); 
	    	                tempFile = File.createTempFile( fileName, ".html" );

	    	                InputStream data = new ByteArrayInputStream( sourceText.getHTMLDocument().getBytes() );
	    	                Files.writeTo( tempFile, data );

	    	            }
	    	            catch ( IOException e )
	    	            {
	    	                throw new RuntimeException( e );
	    	            }

	    	            return tempFile;
	    	        }

		}).setCacheDuration( Duration.NONE ).setDeleteAfterDownload( true );
		exportHtmlLink.setVisible( isAdministrator );

		
		final Label editSubjectTextLabel = new Label( "editsubject", "Edit" );
		if( !isAdministrator ){
			editSubjectTextLabel.setVisible( false );
        }

		final Form<Object> editSubjectTextForm = new Form<Object>( "editsubjecttextform" ) {
			private static final long serialVersionUID = 1L;
		};
		editSubjectTextForm.setVisible( false );

		final TextArea<String> editSubjectTextTA = new TextArea<String>( "editsubjecttext", new Model<String>( sourceSubject.getSubjectText() ) );
		
	    Button saveSubjectTextButton = new Button("savesubjectedit") {
			private static final long serialVersionUID = 1L;    
				
			@Override
			public void onSubmit() {
				SourceSubject aSourceSubject = sourceSubjectDao.find( codeSourceSubject );
				aSourceSubject.setSubjectText( editSubjectTextTA.getInput() );
				if( sourceSubjectDao.update( aSourceSubject ) ){
					DBHelper.getTransaction().commit();
				}else{
					DBHelper.getTransaction().rollback();
					setErrorMessage( "Failed to save subject text" );
				}
				
				editSubjectTextForm.setVisible( false );
				editSubjectTextLabel.setVisible( true );

				setResponsePage( new SourceSubjectPage() );
				return;
		    }
	    };
	    saveSubjectTextButton.setDefaultFormProcessing( false );

	    Button cancelSubjectTextButton = new Button( "cancelsubjectedit" ) {
			private static final long serialVersionUID = 1L;    
				
			@Override
			public void onSubmit() {
				editSubjectTextForm.setVisible( false );
				editSubjectTextLabel.setVisible( true );
				setResponsePage( new SourceSubjectPage() );
				return;
		    }
	    };
	    cancelSubjectTextButton.setDefaultFormProcessing( false );
    	
	    editSubjectTextForm.add( editSubjectTextTA );		
	    editSubjectTextForm.add( saveSubjectTextButton );
	    editSubjectTextForm.add( cancelSubjectTextButton );
		
	    editSubjectTextLabel.add( new AjaxEventBehavior( "onclick" ) {
			private static final long serialVersionUID = 1L;
			@Override
	        protected void onEvent( AjaxRequestTarget target ) {
				editSubjectTextForm.setVisible( true );
				editSubjectTextLabel.setVisible( false );
				setResponsePage( getPage() );
				return;
	        }
        } );

	    
		// This one is HTML. It should only be editable for an administrator
		final MultiLineLabel htmlDocumentLabel = new MultiLineLabel( "htmldocument", StringLogics.prepareSourceText( sourceText.getHTMLDocument() ) );
		htmlDocumentLabel.setEscapeModelStrings( false );

		final Form<Object> editSourceTextForm = new Form<Object>( "editsourcetextform" ) {
			private static final long serialVersionUID = 1L;
		};
		editSourceTextForm.setVisible( false );
		
		final TextArea<String> editSourceTextTA = new TextArea<String>( "editsourcetext", new Model<String>( sourceText.getHTMLDocument() ) );

		final Label editSourceTextLabel = new Label( "edittext", "Edit" );
		if( !isAdministrator ){
        	editSourceTextLabel.setVisible( false );
        }
		
	    Button saveButton = new Button("saveedit") {
			private static final long serialVersionUID = 1L;    
				
			@Override
			public void onSubmit() {
				sourceText.setHTMLDocument( editSourceTextTA.getInput() );
				if( sourceTextDao.update( sourceText ) ){
					DBHelper.getTransaction().commit();
				}else{
					DBHelper.getTransaction().rollback();
				}
				htmlDocumentLabel.setDefaultModel( new Model<String>( StringLogics.prepareSourceText( sourceText.getHTMLDocument() ) ) );
				editSourceTextForm.setVisible( false );
				editSourceTextLabel.setVisible( true );
				htmlDocumentLabel.setVisible( true );
				setResponsePage( new SourceSubjectPage() );
				return;
		    }
	    };
	    saveButton.setDefaultFormProcessing( false );

	    Button cancelButton = new Button( "canceledit" ) {
			private static final long serialVersionUID = 1L;    
				
			@Override
			public void onSubmit() {
				editSourceTextForm.setVisible( false );
				editSourceTextLabel.setVisible( true );
				htmlDocumentLabel.setVisible( true );
				setResponsePage( new SourceSubjectPage() );
				return;
		    }
	    };
	    cancelButton.setDefaultFormProcessing( false );
    	
        editSourceTextLabel.add( new AjaxEventBehavior( "onclick" ) {
			private static final long serialVersionUID = 1L;
			@Override
	        protected void onEvent( AjaxRequestTarget target ) {
				editSourceTextForm.setVisible( true );
				editSourceTextLabel.setVisible( false );
				htmlDocumentLabel.setVisible( false );
				setResponsePage( getPage() );
				return;
	        }
        } );

	    editSourceTextForm.add( editSourceTextTA );		
	    editSourceTextForm.add( saveButton );
	    editSourceTextForm.add( cancelButton );

        Label backLabel = new Label( "back", "<<< Back <<<" );
        backLabel.add( new AjaxEventBehavior("onclick") {
			private static final long 	serialVersionUID 		= 1L;       			
			
			@Override
	        protected void onEvent(AjaxRequestTarget target) {
				setResponsePage( new SourceCategoryPage() );
				return;
	        }
        } );

        add( backLabel );
        
        add( new Label( "errormessage", getSession().getAttribute( "errormessage" ) ) );
       
        htmlForm.add( importHtmlFileNameUF );
        htmlForm.add( importHtmlButton );
        add( htmlForm );

        add( exportHtmlLink );
           
        add( new Label( "description", sourceSubject.getDescription() ) );
        Label subjectTextDescription = new Label( "subjecttextdescription", StringLogics.prepareSourceText( sourceSubject.getSubjectText() ) ); 
        subjectTextDescription.setEscapeModelStrings( false );
		add( subjectTextDescription );

		add( sourceTypeListView );

		add( editSubjectTextForm );
		add( editSubjectTextLabel );
		
		add( htmlDocumentLabel );
		
		add( editSourceTextForm );
        add( editSourceTextLabel );
    	
    	add( deletePanel ); 
	    
	    resetErrorMessage();
	} 		
}
