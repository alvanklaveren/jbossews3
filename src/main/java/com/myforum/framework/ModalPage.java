package com.myforum.framework;

import org.apache.wicket.PageReference;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebPage;
//import org.apache.wicket.protocol.https.RequireHttps;

import com.myforum.base.BasePage;
import com.myforum.base.dictionary.EText;
import com.myforum.base.dictionary.Translator;


//@RequireHttps
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
	
	public void setErrorMessage( String errorMessage ){
		getSession().setAttribute( "errormessage", Translator.getInstance().translate( errorMessage ) );
	}

	public void setErrorMessage( EText eText ){
		getSession().setAttribute( "errormessage", Translator.getInstance().translate( eText.toString() ) );
	}

	public void resetErrorMessage(){
		String nullString = null;
		setErrorMessage( nullString );
	}
}