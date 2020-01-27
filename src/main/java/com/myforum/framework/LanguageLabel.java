package com.myforum.framework;


import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myforum.dictionary.ELanguage;
import com.myforum.dictionary.Translator;

public class LanguageLabel extends Label {
	private static final long serialVersionUID = 1L;
	
   	private static Logger log = LoggerFactory.getLogger(LanguageLabel.class);

	private LanguageLabel(String id) {
		super(id);
	}

	public LanguageLabel(String id,final ELanguage language){
		super(id, "");
		add(new AjaxEventBehavior("onclick"){
			private static final long serialVersionUID = 1L;
	
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				Translator.getInstance().setDefaultLanguage(language);
				log.debug("Language has changed to: " + language.toString());
				
				setResponsePage( target.getPage().getClass(), target.getPage().getPageParameters() );

				return;
			}			
		});

		String labelText  = "<img src=\"" + language.getFontFile() + "\" width=20px height=20px alt=\"" + language.getIsoA2() + "\">";
		if( Translator.getInstance().getDefaultLanguage() == language){
			add(new AttributeAppender("class", "btn btn-secondary"));
			setDefaultModel(new Model<String>("<b>" + labelText + "</b>"));		
		} else {
			setDefaultModel(new Model<String>(labelText));					
		}
		
		setEscapeModelStrings(false); // necessary, to get it displayed in BOLD
	}


	
}
