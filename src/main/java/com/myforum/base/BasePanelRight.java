package com.myforum.base;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;

import com.myforum.application.AllConstants;
import com.myforum.base.dictionary.EText;
import com.myforum.base.dictionary.Translator;

public class BasePanelRight extends Panel{
	private static final long serialVersionUID = 1L;
	
	private Translator translator = Translator.getInstance();
	
	public BasePanelRight(String id){
		super(id);

		add( new Label("aboutwebsite", translator.translate(EText.ABOUT_WEBSITE)) );

		add( new Label("aboutwebsitetext", translator.translate(EText.ABOUT_WEBSITE_TEXT)).setEscapeModelStrings(false) );
		
		String emailAddress = AllConstants.MY_EMAIL;
		addOrReplace(new ExternalLink("myemail", "mailto:" + emailAddress, translator.translate("message")) );

	}

}
