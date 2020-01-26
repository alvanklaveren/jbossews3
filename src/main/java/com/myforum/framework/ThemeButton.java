package com.myforum.framework;


import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myforum.application.CookieLogics;
import com.myforum.application.ETheme;

public class ThemeButton extends Button {
	private static final long serialVersionUID = 1L;
	
   	private static Logger log = LoggerFactory.getLogger(ThemeButton.class);

	private ThemeButton(String id) {
		super(id);
	}

	public ThemeButton(String id, String labelText, final ETheme theme){
		
		this(id, labelText, theme, false);
	}

	public ThemeButton(String id, String labelText, final ETheme theme, boolean setActive /* is used only when cookie is missing*/){
		
		super(id);
		add(new AjaxEventBehavior("onclick"){
			private static final long serialVersionUID = 1L;
	
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				CookieLogics.setCookieForever(ETheme.cookieName, theme.getId());
				log.debug("Language has changed to: " + theme.toString());
				
				setResponsePage( target.getPage().getClass(), target.getPage().getPageParameters() );

				return;
			}			
		});

		if(theme.getId() == CookieLogics.getCookieInt(ETheme.cookieName) || setActive) {
			add(new AttributeAppender("class", "btn btn-secondary"));
			setDefaultModel(new Model<String>("<b>" + labelText + "</b>"));
		} else {
			setDefaultModel(new Model<String>(labelText ));
		}
		
		setEscapeModelStrings(false); // necessary, to get it displayed in BOLD
	}


	
}
