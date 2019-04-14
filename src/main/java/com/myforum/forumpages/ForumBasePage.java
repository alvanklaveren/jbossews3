package com.myforum.forumpages;

import com.myforum.application.CookieLogics;
import com.myforum.base.AVKPage;
import com.myforum.base.menu.EMenuItem;

public class ForumBasePage extends AVKPage{
	private static final long 	serialVersionUID = 1L;
	private ForumBasePanel		activePanel;

	public ForumBasePage(){
		super(EMenuItem.Forum);

		// the BasePage is just a container, so where to go depends on cookies set or not
    	activePanel = getPanelByCookies();
        addOrReplace(activePanel);       
	}   

	public ForumBasePanel getActivePanel(){
		return activePanel;
	}
	

	private ForumBasePanel getPanelByCookies(){
    	if( isMessage() ){ return new ForumMessagePanel(this); }
    	
    	if( isCategory() ){ return new ForumCategoryPanel(this); }

    	// default panel
    	return new ForumHomePanel(this);
	}
	
    private boolean isCategory(){
		if(CookieLogics.getCookieInt("codeMessageCategory") != 0){
    		if(CookieLogics.getCookieInt("codeMessage") <= 0){
    			return true;
    		}
		}   	
		return false;
    }

    private boolean isMessage(){
    	if(CookieLogics.getCookieInt("codeMessageCategory") != 0){ 
    		if(CookieLogics.getCookieInt("codeMessage") > 0){
	    			return true;
    		}
    	}
    	return false;
    }

    @Override
    protected String getPageTitle() {
    	return "AVK - Forum";
    }

    
	@Override
	protected void onBeforeRender() {
        super.onBeforeRender();
    }
	
}

   