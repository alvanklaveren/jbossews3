package com.myforum.framework;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.myforum.base.BasePage;

public class ResponseFormButton extends Button{
	private static final long serialVersionUID = 1L;

	Class<? extends BasePage> responseClass;
	BasePage parent;
	PageParameters params;
	
	public ResponseFormButton(String id, Class<? extends BasePage> responseClass, PageParameters params, boolean visible) {
		super(id);
		this.responseClass = responseClass;
		this.params = params;
		setDefaultFormProcessing(false);
        setVisible(visible);
	}

	public ResponseFormButton(String id, BasePage parent) {
		super(id);
		this.parent = parent;
		setDefaultFormProcessing(false);
	}

	@Override
	public void onSubmit() {
		if( parent != null){
			parent.render();
		}else{	
			if(params == null){
				setResponsePage(responseClass);
				return;
			}else{
				setResponsePage(responseClass, params);
				return;
			}
		}
    }
}
