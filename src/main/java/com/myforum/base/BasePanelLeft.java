package com.myforum.base;

import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.ContextRelativeResource;

public class BasePanelLeft extends Panel{
	private static final long serialVersionUID = 1L;

	protected Image meImage;
	
	public BasePanelLeft(String id){
		super(id);	
		meImage = new Image("me", new ContextRelativeResource("images/me.jpg"));
		add( meImage );
	}

}
