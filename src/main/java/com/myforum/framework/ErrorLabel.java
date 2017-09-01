package com.myforum.framework;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import com.myforum.application.StringLogics;

public class ErrorLabel extends Label{
	private static final long serialVersionUID = 1L;

	public ErrorLabel() {
		super("errormessage");
		String errorMessage = (String) getSession().getAttribute("errormessage");
		if(StringLogics.isEmpty(errorMessage)){
			setVisible(false);
		}else{
			Model<String> errorModel = new Model<String>(errorMessage);
			setDefaultModel(errorModel);
			setVisible(true);
		}
		// immediately remove the message, as to not display it more than once
		getSession().setAttribute("errormessage", "");
	}
}
