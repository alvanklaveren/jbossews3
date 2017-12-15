package com.myforum.framework;

import org.apache.wicket.markup.html.panel.Panel;

public class AVKPanel extends Panel{
	private static final long serialVersionUID = 1L;

	public AVKPanel(String id) {
		super(id);
		
		// Not all panels will show error messages, but the ones that do (and should) have different "ideal" locations to show this error,
		// so we allow putting it in each panel manually, but we do not want to explicitly repeat the code for it, hence the below try-catch
		try {
				addOrReplace( new ErrorLabel() );
		}catch(Exception e) {
			// do nothing... it is okay if it did not succeed
		}
	}

}
