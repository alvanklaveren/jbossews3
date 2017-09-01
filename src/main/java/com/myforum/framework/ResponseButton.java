package com.myforum.framework;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Button;

import com.myforum.base.BasePage;

public class ResponseButton extends Button{
	private static final long serialVersionUID = 1L;

	public ResponseButton(String id, final Class<? extends BasePage> responseClass) {
		super(id);
		setDefaultFormProcessing(false);
		add( new AjaxEventBehavior( "onclick" ) {
			private static final long serialVersionUID = 1L;
			@Override
		    protected void onEvent( AjaxRequestTarget target ) {
		        setResponsePage( responseClass );
		        return;
		    }
		});		
	}
}