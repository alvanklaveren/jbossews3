package com.myforum.application;

import org.apache.wicket.request.resource.CssResourceReference;

public class CustomCssReference extends CssResourceReference{
	
	private static final long serialVersionUID = 1L;
	public static final CustomCssReference INSTANCE = new CustomCssReference();

	public CustomCssReference( ) {
		super( CustomCssReference.class, "basepage.css" );
	}

	
}
