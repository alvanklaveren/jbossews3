package com.myforum.framework;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import com.myforum.application.StringLogics;
import com.myforum.base.dictionary.Translator;

public class PanelNotificationLabel extends Label{
	private static final long serialVersionUID = 1L;

	public PanelNotificationLabel() {
		super("panelnotificationmessage");
		String errorMessage = (String) getSession().getAttribute("panelnotificationmessage");
		if(StringLogics.isEmpty(errorMessage)){
			setVisible(false);
		}else{
			Model<String> errorModel = new Model<String>(Translator.getInstance().translate(errorMessage));
			setDefaultModel(errorModel);
			setVisible(true);
		}
		// do NOT remove the message, because inherited function will duplicate the call to panelerrorlabel
		// reset this in basepage !!
		//getSession().setAttribute("panelerrormessage", "");
	}
}
