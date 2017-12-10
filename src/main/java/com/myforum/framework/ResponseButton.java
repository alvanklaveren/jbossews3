package com.myforum.framework;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.Model;

import com.myforum.base.BasePage;

public class ResponseButton extends Button{
	private static final long serialVersionUID = 1L;
	private Class<? extends BasePage> responseClass;

	public ResponseButton(String id, Model<String> buttonText, final Class<? extends BasePage> responseClass) {
		super(id, buttonText);
		setDefaultFormProcessing(false);
		this.responseClass = responseClass;
	}
	
	@Override
	public void onSubmit() {
        setResponsePage( responseClass );
        return;	
	}

}