package com.myforum.framework;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import com.myforum.base.dictionary.Translator;

public class AVKLabel extends Label{
	private static final long serialVersionUID = 1L;

	public AVKLabel(String id, String labelText) {
		super(id, new Model<String>(Translator.getInstance().translate(labelText)));
	}

}
