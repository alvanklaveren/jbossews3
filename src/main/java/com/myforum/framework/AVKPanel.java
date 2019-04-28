package com.myforum.framework;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;

import com.myforum.dictionary.Translator;

public class AVKPanel extends Panel{
	private static final long serialVersionUID = 1L;
	protected Translator translator = Translator.getInstance();
	
	public AVKPanel(String id) {
		super(id);
		
		// Error Label
		WebMarkupContainer panelErrorDiv = new WebMarkupContainer("panelerrordiv");
		addOrReplace( panelErrorDiv );

		PanelErrorLabel panelErrorLabel = new PanelErrorLabel();
		panelErrorDiv.addOrReplace( panelErrorLabel );

		panelErrorDiv.setVisible(panelErrorLabel.isVisible());
		
		getSession().setAttribute( "panelerrormessage", "");	
	}

}
