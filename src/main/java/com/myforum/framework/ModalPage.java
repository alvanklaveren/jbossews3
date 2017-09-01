package com.myforum.framework;

import org.apache.wicket.PageReference;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.https.RequireHttps;

import com.myforum.base.BasePage;


@RequireHttps
public class ModalPage extends WebPage {
	private static final long serialVersionUID = 1L;

	protected ModalWindow parent;
	protected BasePage originPage;
	protected PageReference modalWindowPageRef;

    public ModalPage(final PageReference modalWindowPageRef, BasePage originPage) {
    	this.modalWindowPageRef = modalWindowPageRef;
    	this.originPage = originPage;
    }
    
    public void setParent(ModalWindow parent){
    	this.parent = parent;
    	parent.setTitle(getTitle());
    }
    
	protected String getTitle(){
		return "No title defined"; 
	}
}