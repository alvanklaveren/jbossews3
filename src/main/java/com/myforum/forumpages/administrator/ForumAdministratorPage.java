package com.myforum.forumpages.administrator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.DynamicImageResource;

import com.myforum.application.AllConstants;
import com.myforum.application.DBHelper;
import com.myforum.application.ForumUtils;
import com.myforum.application.IoLogics;
import com.myforum.application.StringLogics;
import com.myforum.base.AVKPage;
import com.myforum.base.menu.EMenuItem;
import com.myforum.forumpages.ForumBasePage;
import com.myforum.forumpages.UserModifyAccountPage;
import com.myforum.framework.AVKButton;
import com.myforum.tables.Classification;
import com.myforum.tables.Constants;
import com.myforum.tables.ForumUser;
import com.myforum.tables.MessageCategory;
import com.myforum.tables.dao.ClassificationDao;
import com.myforum.tables.dao.ConstantsDao;
import com.myforum.tables.dao.ForumUserDao;
import com.myforum.tables.dao.MessageCategoryDao;

public class ForumAdministratorPage extends AVKPage {
	private static final long 	serialVersionUID = 1L;

	public ForumAdministratorPage(){
		super(EMenuItem.Forum);
    	// create a list of ITab objects used to feed the tabbed panel
        List<ITab> tabs = new ArrayList<ITab>();
        tabs.add(new AbstractTab(new Model<String>("Constants"))
        {
			private static final long serialVersionUID = 1L;

			@Override
            public Panel getPanel(String panelId)
            {
                return new TabPanel1(panelId);
            }
        });

        tabs.add(new AbstractTab(new Model<String>("Users"))
        {
			private static final long serialVersionUID = 1L;

			@Override
            public Panel getPanel(String panelId)
            {
                return new TabPanel2(panelId);
            }
        });

        tabs.add(new AbstractTab(new Model<String>("Forum Utilities"))
        {
			private static final long serialVersionUID = 1L;

			@Override
            public Panel getPanel(String panelId)
            {
                return new TabPanel3(panelId);
            }
        });

        add(new AjaxTabbedPanel<ITab>("tabs", tabs));
    }
 
    private static class TabPanel1 extends Panel
    {
		private static final long 	serialVersionUID 	= 1L;
		private final ConstantsDao	constantsDao 		= new ConstantsDao(); 

        public TabPanel1( String id )
        {
    			super( id );	
    			
    	    	final Constants guestImage 		= constantsDao.findById("guestimage");
    	    	final Constants webLogo 		= constantsDao.findById("weblogo");
    	    	final Constants acmeAddress 	= constantsDao.findById("acme_address");
    	    	final Constants acmeId 			= constantsDao.findById("acme");

    				
    			// Form for add message button
    	    	StatelessForm<Object> form = new StatelessForm<Object>( "constantsform" ) {
    				private static final long serialVersionUID = 1L;
    			};    	  

    	    	final TextField<String> acmeAddressField = new TextField<String>("acme_address", new Model<String>(acmeAddress.getStringValue()) );
    	    	form.add( acmeAddressField );
    	    	
    	    	final TextField<String> acmeIdField 	 = new TextField<String>("acme_id", new Model<String>(acmeId.getStringValue()) );
    	    	form.add( acmeIdField );

    			final FileUploadField avatarfileUF 		 = new FileUploadField( "avatarfile" ); 
    			form.add( avatarfileUF );

    			final FileUploadField webLogoFileUF 	 = new FileUploadField( "weblogofile" ); 
    			form.add( webLogoFileUF );

    			final NonCachingImage ncImage = new NonCachingImage( "avatarpicture", new AbstractReadOnlyModel<DynamicImageResource>(){
    				private static final long serialVersionUID = 1L;

    				@Override 
    				public DynamicImageResource getObject() {
    				    DynamicImageResource dir = new DynamicImageResource() {
    				    	private static final long serialVersionUID = 1L;

    				    	@Override 
    				    	protected byte[] getImageData(Attributes attributes) {
    							return  guestImage.getBlobValue();
    				    	}

    				    };
    				    return dir;
    				}
    			});
    			form.add( ncImage );

    			
    			final NonCachingImage weblogoImage = new NonCachingImage( "weblogopicture", new AbstractReadOnlyModel<DynamicImageResource>(){
    				private static final long serialVersionUID = 1L;

    				@Override 
    				public DynamicImageResource getObject() {
    				    DynamicImageResource dir = new DynamicImageResource() {
    				    	private static final long serialVersionUID = 1L;

    				    	@Override 
    				    	protected byte[] getImageData(Attributes attributes) {
    							return  webLogo.getBlobValue();
    				    	}

    				    };
    				    return dir;
    				}
    			});
    			form.add( weblogoImage );

    			Button uploadButton = new AVKButton( "uploadbutton", "Upload File" ){
    				private static final long serialVersionUID = 1L;

    				@Override
					public void onSubmit() {
    					if(avatarfileUF != null && avatarfileUF.getFileUpload() != null){
	    					byte[] imageData = IoLogics.readImageFile( avatarfileUF, "" );	
	    					if( imageData.length > 0 ){ 
	    						guestImage.setBlobValue( imageData );
	    					}
    					}

    					if(webLogoFileUF != null && webLogoFileUF.getFileUpload() != null){
	    					byte[] imageData = IoLogics.readImageFile( webLogoFileUF, "" );	
	    					if( imageData.length > 0 ){ 
	    						webLogo.setBlobValue( imageData );
	    					}
    					}
    				}
    			};
    			form.add(uploadButton);
    		
    		    Button button = new AVKButton( "submit", "Apply Changes" ) {
    				private static final long serialVersionUID = 1L;

    				@Override
					public void onSubmit() {   			
    					acmeAddress.setStringValue( ForumUtils.getInput(acmeAddressField) );
    					acmeId.setStringValue( ForumUtils.getInput(acmeIdField) );
    					
    					DBHelper.saveAndCommit(acmeAddress);
    					DBHelper.saveAndCommit(acmeId);
    					DBHelper.saveAndCommit(guestImage);
    					DBHelper.saveAndCommit(webLogo);
    					ForumUtils.reloadWebLogo();
  		        	
    		        	setResponsePage( new ForumBasePage() );
    		        	return;
    			    }
    			};
    		    button.setDefaultFormProcessing( false );
    		    form.add( button );
    			add( form );		
    	}
    		
    }	                	
      
    private static class TabPanel2 extends Panel
    {
		private static final long 			serialVersionUID 	= 1L;
		private final ForumUserDao 			forumUserDao 		= new ForumUserDao();
		private final ClassificationDao 	classificationDao 	= new ClassificationDao();
		private List<ForumUser> 			userList;
		
		public TabPanel2( String id )
        {
            super(id);

	    	userList = forumUserDao.list();
    	
    	    Collections.sort(userList, new Comparator<ForumUser>() {
    	    	@Override
    	        public int compare(ForumUser f1, ForumUser f2) {
    	        	// Sort on Date. If date equal then sort on primary key code
    	            int dateDifference = f2.getUsername().compareTo(f1.getUsername());
    	            if(dateDifference != 0) return dateDifference;
    	            return f2.getCode() - f1.getCode();       
    	        }

    	    }); 
    	    	    
    	    PageableListView <ForumUser> listView = new PageableListView<ForumUser>("userlist", userList, 30) {
    			private static final long serialVersionUID = 1L;

    			@Override
    			public void populateItem(final ListItem<ForumUser> listItem) {
    				final ForumUser forumUser = listItem.getModelObject();
    				Classification classification = classificationDao.find( forumUser.getClassification().getCode() );
    				
                    listItem.add(new Label( "username", forumUser.getUsername() ));
                    listItem.add(new Label( "password", "..." ));
                    listItem.add(new Label( "classification", classification.getDescription() ));

                    listItem.add( new AjaxEventBehavior( "onclick") {
    					private static final long serialVersionUID = 1L;
    					@Override
                        protected void onEvent(AjaxRequestTarget target) {
    						String urlEncoded = AllConstants.getCrypt().encryptUrlSafe(String.valueOf( forumUser.getCode() ));
    						PageParameters parameters = new PageParameters().set("codeforumuser", urlEncoded);
    		                setResponsePage( UserModifyAccountPage.class, parameters );
    		                return;
                        }
                    });
    			}
            };
    		
            add( listView ).setVersioned(false); 

            add(new PagingNavigator("navigator", listView));

        }
    }
 
    private static class TabPanel3 extends Panel
    {
		private static final long 				serialVersionUID 	= 1L;
		private final 		 MessageCategoryDao messageCategoryDao	= new MessageCategoryDao();

		public TabPanel3( String id )
        {
            super( id );
			// Form for add message button
            StatelessForm<Object> form = new StatelessForm<Object>( "categoryform" ) {
				private static final long serialVersionUID = 1L;
			};    	  

			final Label label = new Label( "label", new Model<String>("New Category"));
			form.add(label);
			
	        final TextField<String> categoryDescriptionTF = new TextField<String>( "description", new Model<String>() ); 
			form.add( categoryDescriptionTF );

		    Button addCategoryButton = new AVKButton( "addcategory", "Add Category" ) {
				private static final long serialVersionUID = 1L;

				@Override
				public void onSubmit() {
					if( categoryDescriptionTF == null || StringLogics.isEmpty( categoryDescriptionTF.getInput() ) ){
						return;
					}
			    	if ( messageCategoryDao.find( categoryDescriptionTF.getInput() ).size() > 0 ) {
			    		// if description already exists, then stop here
			    		return;
			    	}
			    	MessageCategory messageCategory = new MessageCategory();
			    	messageCategory.setDescription( categoryDescriptionTF.getInput() );
			    	messageCategoryDao.add( messageCategory );

			    	setResponsePage( new ForumAdministratorPage() );
			    	return;
			    }
			};
			
			addCategoryButton.setDefaultFormProcessing( false );
		    form.add(addCategoryButton);
		    
			add(form);		
        }
    }
}
