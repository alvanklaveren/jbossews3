package com.myforum.application;

import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.codec.binary.Base64;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.AbstractTextComponent;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myforum.base.BasePage;
import com.myforum.framework.ModalPage;
import com.myforum.tables.ForumUser;


public final class ForumUtils{
	
	static{ new ForumUtils(); }

   	private static Logger log = LoggerFactory.getLogger(ForumUtils.class);
   	private static NonCachingImage webLogo = loadWebLogo("weblogo");

	private ForumUtils(){}
	
	public static NonCachingImage loadAvatar( final ForumUser forumUser, String wicketID ){
		// Always use non caching image, to prevent cached pictures to be allocated to the wrong user
		return( new NonCachingImage(wicketID, new AbstractReadOnlyModel<DynamicImageResource>(){
					private static final long serialVersionUID = 1L;
		
					@Override 
					public DynamicImageResource getObject() {
					    DynamicImageResource dir = new DynamicImageResource() {
					    	private static final long serialVersionUID = 1L;
		
					    	@Override 
					    	protected byte[] getImageData(Attributes attributes) {
								byte[] imageData = forumUser.getAvatar();
								if( imageData == null || imageData.length==0 ){
									imageData = AllConstants.getConstantBlobValue("guestimage");
								}
								return  imageData;
					      }
					    };
					    return dir;
					  }
					})
				);			
	}

	public static NonCachingImage getWebLogo(){
		if( webLogo != null ){
			return webLogo; 
		}
		
		return ForumUtils.loadWebLogo("weblogo");
	}
	
	public static NonCachingImage reloadWebLogo(){
		ForumUtils.resetWebLogo();
		return ForumUtils.getWebLogo();
	}

	private static void resetWebLogo(){
		webLogo = null;
	}
		
	private static NonCachingImage loadWebLogo( String wicketID ){
		
		// Always use non caching image, to prevent cached pictures to be allocated to the wrong user
		return( new NonCachingImage(wicketID, new AbstractReadOnlyModel<DynamicImageResource>(){
					private static final long serialVersionUID = 1L;
		
					@Override 
					public DynamicImageResource getObject() {
					    DynamicImageResource dir = new DynamicImageResource() {
					    	private static final long serialVersionUID = 1L;
		
					    	@Override 
					    	protected byte[] getImageData(Attributes attributes) {
					    		return AllConstants.getConstantBlobValue("weblogo");
					      }
					    };
					    return dir;
					  }
					})
				);			
	}
	
	public static NonCachingImage loadImage( final byte[] imageData, String wicketID ){
		// Always use non caching image, to prevent cached pictures to be allocated to the wrong user
		return( new NonCachingImage(wicketID, new AbstractReadOnlyModel<DynamicImageResource>(){
					private static final long serialVersionUID = 1L;
		
					@Override 
					public DynamicImageResource getObject() {
					    DynamicImageResource dir = new DynamicImageResource() {
					    	private static final long serialVersionUID = 1L;
		
					    	@Override 
					    	protected byte[] getImageData(Attributes attributes) {
								return  imageData;
					      }
					    };
					    return dir;
					  }
					})
				);			
	}
	
	
	public static String getAvatarAsStringByCode( String codeForumUser ){
		String encodedString = null;
		Session session = DBHelper.openSession();
		Criteria criteria = session.createCriteria(ForumUser.class);

		criteria.add( Restrictions.eq("code", Integer.parseInt( codeForumUser ) ) );

		if( criteria.list().size() > 0 ){
			byte[] image = ((ForumUser) criteria.list().get(0)).getAvatar();
			if( image == null || image.length == 0 ) { image = AllConstants.getConstantBlobValue("guestimage"); } 
			Base64 base64 = new Base64();
			 encodedString = new String( base64.encode(image) );
		}
		
		session.close();
			
		return encodedString;
	}

	public static boolean isNullOrZero( Integer anInteger ){
		if( anInteger == null || anInteger == 0 ){
			return true;
		}
		return false;
	}

	public static void debugInfo( WebPage webPage ){
	    //System.out.println( webPage.getClass().toString() + ".getStatelessHint() = " + webPage.getStatelessHint() );
	    //System.out.println( webPage.getClass().toString() + ".isPageStateless() = " + webPage.isPageStateless());
	}

	public static void debugInfo(Panel menuPanel) {
		//System.out.println("Create Panel: " + menuPanel.getId() );
	}

	public static String getParmString( PageParameters params, String id, String defaultValue ){
		if(params.get(id) == null){
			return defaultValue;
		}

		String 	paramValue = params.get(id).toString();
	
		if(paramValue == null){
			return defaultValue;
		}
		
		return paramValue;
	}	

	
	public static int getParmInt( PageParameters params, String id, int defaultValue){
		String 	paramValue 	= "";
		int 	intValue 	= defaultValue;

		if(params.get(id) != null){
			paramValue = params.get(id).toString();
			if(paramValue == null){
				return defaultValue;
			}
			try{
				intValue = Integer.parseInt(paramValue);
			}catch(Exception e){
				e.printStackTrace();
				log.error("failed to parse integer value from page parameter in getInt(PageParameters, id)");
			}
		}
		return intValue;
	}

	/*
	 * Return the current date
	 */
	public static Date todayNow(){
		return new Date();
	}
	
	/*
	 * Return a date, based on a format
	 */
	public static Date createDate(String date, String format) {
	     try {
	         return new SimpleDateFormat(format).parse(date);
	     } catch (ParseException e) {
	         return null;
	     }
	  }

	/*
	 * Returns the number of years between two dates
	 */
	public static int yearsBetween(Date first, Date last) {
		final int YEAR = Calendar.YEAR;
		final int MONTH = Calendar.MONTH;
		final int DATE = Calendar.DATE;
	    Calendar a = getCalendar(first);
	    Calendar b = getCalendar(last);
	    int diff = b.get(YEAR) - a.get(YEAR);
	    if (a.get(MONTH) > b.get(MONTH) || 
	        (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
	        diff--;
	    }
	    return diff;
	}
	
	private static Calendar getCalendar(Date date) {
	    Calendar cal = Calendar.getInstance(Locale.ENGLISH);
	    cal.setTime(date);
	    return cal;
	}
	
	/*
	 * 	Overload of getInput(component, defaultValue)
	 *  default return an empty string
	 */
	public static String getInput(AbstractTextComponent<String> component){
		return getInput(component, "");
	}

	/*
	 *  Return the input value of an AbstractTextComponent like TextField
	 *  If value is empty, return the default value
	 */
	public static String getInput(AbstractTextComponent<String> component, String defaultValue){
		String inputValue = component.getInput().trim();
		if(StringLogics.isEmpty(inputValue)){ 
			return defaultValue; 
		}
		
		return inputValue;
	}

	public static ModalWindow createModalWindow(String wicketId, final BasePage parent, final ModalPage modalPage){
        // Create the modal window.
        final ModalWindow modal;
        modal = new ModalWindow(wicketId);
        //modal.setCookieName("modal");
       
        // reset positions, to prevent resizing issues
        //CookieLogics.setCookie("wicket-modal-window-positions", "");
               
        modal.setResizable(false);
        modal.setAutoSize(false);
        modal.setInitialWidth(360);
        modal.setInitialHeight(550);
        
        modalPage.setParent(modal);
        
        modal.setPageCreator(new ModalWindow.PageCreator() {
			private static final long serialVersionUID = 1L;

			@Override
			public Page createPage() {
                // Use this constructor to pass a reference of this page.
                return modalPage;
            }
        });

        modal.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
			private static final long serialVersionUID = 1L;

			public void onClose(AjaxRequestTarget target) {
                // The variable passValue might be changed by the modal window.
                // target.add(passValueLabel);
            }
        });
        
        modal.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
			private static final long serialVersionUID = 1L;

			public boolean onCloseButtonClicked(AjaxRequestTarget target) {
                // Change the passValue variable when modal window is closed.
                // setPassValue("Modal window is closed by user.");
				parent.getSession().setAttribute( "errormessage", "" );

                return true;
            }
        });

        return modal;
	}

	/** helper function to better access resources
	 * 
	 * @param filepath - path to resource file
	 * @param filename - resource file 
	 * @return File found in resources folder "filepath"
	 */
	public static File getFileResource(String filepath, String filename) {
	
    	// filepath + filename (e.g. "articlepages/articles.xml")
    	
		File resourceFile = null;
		
		if(filepath == null || filename == null) {
			
			log.error("filepath or filename in arguments is null");
			return null;
		}
		
		String resource;
		if(!filepath.isEmpty()) {
			if(filepath.endsWith("/") || filepath.endsWith("\\")){
				resource = filepath + filename;
			} else {
				resource = filepath + "/" + filename;
			}
		}else {
			resource = filename;
		}
			
		log.info("Trying to read: " + resource);
		
//		WebApplication webApplication = WebApplication.get();
//		if(webApplication!=null){
//			ServletContext servletContext = webApplication.getServletContext();
//			if(servletContext!=null){
//				//rootContext = servletContext.getServletContextName();
//				//resourcePaths = servletContext.getResourcePaths("/");
//				URL url = null;
//				try {
//					url = servletContext.getResource(resource);
//					String res = url.getFile().replace("%20", " ");
//					resourceFile = new File(res);
//				} catch (MalformedURLException e) {
//					// TODO Auto-generated catch block
//					log.error(e.getMessage());
//					e.printStackTrace();
//				}
//			}else{
//				//do nothing
//			}
//		}else{
//			//do nothing
//		}
		
		URL url = BasePage.class.getResource("/");
		log.info("Resource path = " + url.getPath());
		String res = url.getPath().replace("%20", " ") + resource;
		resourceFile = new File(res);
		
		return resourceFile;
		
	}

	public static File getFileResource(Class<BasePage> class1, String filepath, String filename) {
		// TODO Auto-generated method stub
		return null;
	}

}
