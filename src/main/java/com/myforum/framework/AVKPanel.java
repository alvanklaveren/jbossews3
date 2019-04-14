package com.myforum.framework;

import org.apache.wicket.markup.html.panel.Panel;

import com.myforum.base.dictionary.Translator;

public class AVKPanel extends Panel{
	private static final long serialVersionUID = 1L;
	protected Translator translator = Translator.getInstance();
	
	public AVKPanel(String id) {
		super(id);
	}

}
