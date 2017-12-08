package com.myforum.base;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

import com.myforum.application.CookieLogics;
import com.myforum.homepage.HomePage;

public class AgreeToCookiesPanel extends Panel {
	private static final long serialVersionUID = 1L;
	
	public AgreeToCookiesPanel(final BasePage parent, String id) {
		super(id);

	
		Link<String> seenCookies = new Link<String>("seenCookies"){
			private static final long serialVersionUID = 1L;    

			@Override
			public void onClick() {
				CookieLogics.setCookie("agreedToCookie", "yes");
				setResponsePage( new HomePage() );
				return;
			}
	    };
	    addOrReplace(seenCookies);
	}

	 @Override 
	 protected void onBeforeRender() { 
	  super.onBeforeRender(); 
	 } 
	 
	@Override public void renderHead(IHeaderResponse response){
	}
    
}
