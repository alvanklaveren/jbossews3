package com.myforum.framework;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.myforum.base.AVKPage;
import com.myforum.base.BasePage;

public class ResponseFormButton extends AVKButton{
	private static final long serialVersionUID = 1L;

	Class<? extends BasePage> responseClass;
	BasePage parent;
	PageParameters params;
	String buttonText;
	
	public ResponseFormButton(String id, String buttonText, Class<? extends BasePage> responseClass, PageParameters params, boolean visible) {
		super(id, buttonText);
		this.responseClass = responseClass;
		this.params = params;
		this.buttonText = buttonText;
		setDefaultFormProcessing(false);
        setVisible(visible);
	}

	public ResponseFormButton(String id, String buttonText, BasePage parent) {
		super(id, buttonText);
		this.buttonText = buttonText;
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
