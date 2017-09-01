package com.myforum.base;

import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;

import com.myforum.application.AllConstants;

public class BasePanelRight extends Panel{
	private static final long serialVersionUID = 1L;

	public BasePanelRight(String id){
		super(id);

		String emailAddress = AllConstants.MY_EMAIL;
		addOrReplace(new ExternalLink("myemail", "mailto:" + emailAddress, emailAddress) );

	}

}
