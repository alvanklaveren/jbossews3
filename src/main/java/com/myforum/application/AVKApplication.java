package com.myforum.application;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.https.HttpsConfig;
import org.apache.wicket.protocol.https.HttpsMapper;
import org.apache.wicket.response.filter.AjaxServerAndClientTimeFilter;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.lang.Bytes;

import com.myforum.articlepage.ArticlePage;
import com.myforum.forumpages.ForumBasePage;
import com.myforum.gameshop.GameShopPage;
import com.myforum.gameshop.REST.GameShopMobile;
import com.myforum.gameshop.codetable.CodeTablesPage;
import com.myforum.homepage.AboutMePage;
import com.myforum.homepage.HomePage;


public class AVKApplication extends WebApplication {
    public AVKApplication() {
   		//System.setProperty( "java.awt.headless", "true" );
    }

    @Override
    protected void init(){
    	super.init();
    	
    	// Inject Spring Annotations, Services, etc..
    	getComponentInstantiationListeners().add( new SpringComponentInjector(this) );
    	
    	getApplicationSettings().setUploadProgressUpdatesEnabled( true );
    	getApplicationSettings().setDefaultMaximumUploadSize( Bytes.megabytes( 1 ) );
    	
		getResourceSettings().setThrowExceptionOnMissingResource( false );

		getRequestCycleSettings().addResponseFilter( new AjaxServerAndClientTimeFilter() );

		getDebugSettings().setAjaxDebugModeEnabled( false );
		
		// load css IMMEDIATELY when rebuilding the page TODO: doesn't work the way I want
		//getResourceBundles().addCssBundle( AVKApplication.class, "css/basepage.css",
		//	(CssResourceReference) CustomCssReference.INSTANCE );
	
		  // On wicket session timeout or wicket exception, redirect to main page 
		  getApplicationSettings().setPageExpiredErrorPage(HomePage.class); 
		  getApplicationSettings().setAccessDeniedPage(HomePage.class); 
		  getApplicationSettings().setInternalErrorPage(HomePage.class); 
		 
		  // show internal error page rather than default developer page 
		  // getExceptionSettings().setUnexpectedExceptionDisplay(IExceptionSettings.SHOW_INTERNAL_ERROR_PAGE); 
		
		  //mountPage("/", HomePage.class); // DO NOT USE. will disable bootstrap (css)
		  
		  //mountPage("/sources", SourceHomePage.class);
		  mountPage("/forum", ForumBasePage.class);
		  mountPage("/aboutme", AboutMePage.class);
		  		  
		  // now for some REST stuff supported by Wicket
		  mountPage("/articles/#{id}", ArticlePage.class);
		  mountPage("/gameshop/#{console}/#{type}/#{searchtitle}", GameShopPage.class);
		  mountPage("/codetable/#{codetable}", CodeTablesPage.class);
		  
		  // to REST pages, not inherited from basepage, therefore NOT https !!
		  // acme_address in constants contains the verification code from the HTTPS provider to verify that I am who I say I am
	        String acme_address = AllConstants.getConstantStringValue("acme_address");
	        if( !acme_address.endsWith("/")){
	        	acme_address += "/";
	        }
	        
	        acme_address += "#{acme_address}";
	        
	        if(StringLogics.isEmpty(acme_address)){
	        	acme_address = "/.well-known/pki-validation/address_lost";
	        }

		  //mountPage("/.well-known/acme-challenge/1LXCE9UAw5-3ciPOFvsnLvexpQLtIQRc3KJYIpt9amU", AcmePage.class);
		  mountPage(acme_address, AcmePage.class);
		  

		  mountPage("/gameshopmobile/#{console}/#{type}/#{searchtitle}", GameShopMobile.class );
		  
		  // this package scanner will mount all resource classes that are tagged with @ResourcePath("//...")
		  // PackageScanner.scanPackage("org.wicketstuff.rest.resource");
		  		  
		  setRootRequestMapper(new HttpsMapper(getRootRequestMapper(), new HttpsConfig() ));
		  // the next line activates https (on 443) BUT HttpsConfig() already adds 443 these by default
		  //setRootRequestMapper(new HttpsMapper(getRootRequestMapper(), new HttpsConfig(80, 443) ));
    }
   
    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<? extends Page> getHomePage() {
        return HomePage.class;
    }
    
}