package com.myforum.base;

import java.io.Serializable;

import com.myforum.base.menu.EMenuItem;

public class AVKPage extends BasePage implements Serializable{
	private static final long 	serialVersionUID = 1L;

	/**
	 *  Again a BASE class, but this time includes predefined JQUERY class
	 *  This cannot be used when using gameshop page AUTOCOMPLETE, because 
	 *  googlecode.wicket-jquery-ui uses its own built in JQUERY (otherwise it does not work)
	 */
	public AVKPage(){ }

	public AVKPage(EMenuItem menuItem){
		super(menuItem);
	}

}
