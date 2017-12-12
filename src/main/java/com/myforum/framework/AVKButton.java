package com.myforum.framework;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.Model;

import com.myforum.base.dictionary.Translator;

public class AVKButton extends Button {
	private static final long serialVersionUID = 1L;

	public AVKButton(String id, String buttonText) {
		super(id, new Model<String>(Translator.getInstance().translate(buttonText)));
	}

}
