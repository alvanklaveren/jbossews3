package com.myforum.framework;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.myforum.base.BasePage;
import com.myforum.base.dictionary.Translator;

public class ResponseFormButton extends Button{
	private static final long serialVersionUID = 1L;

	Class<? extends BasePage> responseClass;
	BasePage parent;
	PageParameters params;
	String buttonText;
	
	public ResponseFormButton(String id, String buttonText, Class<? extends BasePage> responseClass, PageParameters params, boolean visible) {
		super( id, new Model<String>(Translator.getInstance().translate(buttonText)) );
		this.responseClass = responseClass;
		this.params = params;
		this.buttonText = buttonText;
		setDefaultFormProcessing(false);
        setVisible(visible);
	}

	public ResponseFormButton(String id, String buttonText, BasePage parent) {
		super(id, new Model<String>(Translator.getInstance().translate(buttonText)) );
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
