package com.myforum.framework;


import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
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

	public LanguageLabel(String id, String labelText, final ELanguage language){
		super(id, labelText);
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

		if( Translator.getInstance().getDefaultLanguage() == language){ 
			setDefaultModel(new Model<String>("<b>" + labelText + "</b>")); 
		}
		setEscapeModelStrings(false); // necessary, to get it displayed in BOLD
	}


	
}
