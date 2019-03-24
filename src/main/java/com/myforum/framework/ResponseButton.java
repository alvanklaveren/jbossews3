package com.myforum.framework;

import com.myforum.base.AVKPage;

public class ResponseButton extends AVKButton{
	private static final long serialVersionUID = 1L;
	private Class<? extends AVKPage> responseClass;

	public ResponseButton(String id, String buttonText, final Class<? extends AVKPage> responseClass) {
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