package com.myforum.framework;

import org.apache.wicket.PageReference;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;

import com.myforum.base.BasePage;
import com.myforum.base.IRequiresHttps;

public class ModalPanel extends AVKPanel implements IRequiresHttps {
	private static final long serialVersionUID = 1L;

	protected ModalWindow parent;
	protected BasePage originPage;
	protected PageReference modalWindowPageRef;

    public ModalPanel(String id, final PageReference modalWindowPageRef, BasePage originPage) {
    	super(id);
    	this.modalWindowPageRef = modalWindowPageRef;
    	this.originPage = originPage;
    	
		// Not all modal windows will show error messages, but the ones that do (and should) have different "ideal" locations to show this error,
		// so we allow putting it in each modal manually, but we do not want to explicitly repeat the code for it, hence the below try-catch
		try {
				addOrReplace( new ErrorLabel() );
		}catch(Exception e) {
			// do nothing... it is okay if it did not succeed
		}
    }
    
    public void setParent(ModalWindow parent){
    	this.parent = parent;
    	parent.setTitle(getTitle());
    }
    
	protected String getTitle(){
		return "No title defined"; 
	}
}