package com.myforum.framework;

import org.apache.wicket.markup.html.panel.Panel;

import com.myforum.base.dictionary.EText;
import com.myforum.base.dictionary.Translator;

public class AVKPanel extends Panel{
	private static final long serialVersionUID = 1L;
	protected Translator translator = Translator.getInstance();
	
	public AVKPanel(String id) {
		super(id);

		// Not all panels will show error messages, but the ones that do (and should) have different "ideal" locations to show this error,
		// so we allow putting it in each panel manually, but we do not want to explicitly repeat the code for it, hence the below try-catch
		try {
				addOrReplace( new PanelErrorLabel() );
				addOrReplace( new PanelNotificationLabel() );
		}catch(Exception e) {
			// do nothing... it is okay if it did not succeed
		}
	}

	protected void setErrorMessage( String errorMessage ){
		getSession().setAttribute( "panelerrormessage", translator.translate(errorMessage) );
	}
	
	protected void setErrorMessage( EText eText ){
		getSession().setAttribute( "panelerrormessage", translator.translate(eText.toString()) );
	}

	protected void setNotificationMessage( String errorMessage ){
		getSession().setAttribute( "panelnotificationmessage", translator.translate(errorMessage) );
	}

	protected void setNotificationMessage( EText eText ){
		getSession().setAttribute( "panelnotificationmessage", translator.translate(eText.toString()) );
	}

}
