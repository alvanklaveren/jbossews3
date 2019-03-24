package com.myforum.gameshop;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;

import com.myforum.base.AVKPage;
import com.myforum.base.dictionary.Translator;

public class ResponseFormModalButton extends AjaxButton{
	private static final long serialVersionUID = 1L;

	ModalWindow	modal;
	AVKPage parent;
	
	public ResponseFormModalButton(String id, String buttonText, final ModalWindow modal, boolean visible) {
		super(id, new Model<String>(Translator.getInstance().translate(buttonText)) );
		this.modal = modal;
		setDefaultFormProcessing(false);
		setOutputMarkupId(true);
	    setVisible(visible);
	}

	@Override
	public void onSubmit(AjaxRequestTarget target, Form<?> form) {
		modal.show(target);
		return;
	}

}
