package com.myforum.framework;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import com.myforum.application.StringLogics;
import com.myforum.dictionary.Translator;

public class ErrorLabel extends Label{
	private static final long serialVersionUID = 1L;

	public ErrorLabel() {
		super("errormessage");
		String errorMessage = (String) getSession().getAttribute("errormessage");
		if(StringLogics.isEmpty(errorMessage)){
			setVisible(false);
		}else{
			Model<String> errorModel = new Model<String>(Translator.getInstance().translate(errorMessage));
			setDefaultModel(errorModel);
			setVisible(true);
		}
		// do NOT remove the message, because inherited function will duplicate the call to errorlabel reset this in basepage !!
		// getSession().setAttribute("errormessage", "");
	}
}
