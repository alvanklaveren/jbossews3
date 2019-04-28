package com.myforum.framework;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import com.myforum.application.StringLogics;
import com.myforum.dictionary.Translator;

public class SuccessLabel extends Label{
	private static final long serialVersionUID = 1L;

	public SuccessLabel() {
		super("successmessage");
		String errorMessage = (String) getSession().getAttribute("successmessage");
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
