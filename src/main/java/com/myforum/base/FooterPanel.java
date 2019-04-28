package com.myforum.base;

import java.text.DateFormat;
import java.util.Date;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.myforum.application.AllConstants;
import com.myforum.dictionary.EText;
import com.myforum.dictionary.Translator;

public class FooterPanel extends Panel{
	private static final long serialVersionUID = 1L;

	private Translator translator = Translator.getInstance();
	
	public FooterPanel(String id){
		super(id);	

		Label today = new Label("today", new Model<String>("today"){
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject(){
				Date today = new Date();
				// now return the date in the 'locale' format
				return DateFormat.getDateInstance().format(today);
			}
			
		});
		
		add(today);
		
		add( new Label("aboutwebsitetext", translator.translate(EText.ABOUT_WEBSITE_TEXT)).setEscapeModelStrings(false) );
		
		String emailAddress = AllConstants.MY_EMAIL;
		addOrReplace(new ExternalLink("myemail", "mailto:" + emailAddress, translator.translate("message")) );
	}

}
