package com.myforum.base;

import java.text.DateFormat;
import java.util.Date;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

public class FooterPanel extends Panel{
	private static final long serialVersionUID = 1L;

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
	}

}
