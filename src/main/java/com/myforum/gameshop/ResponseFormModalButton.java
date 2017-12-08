package com.myforum.gameshop;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;

import com.myforum.base.BasePage;

public class ResponseFormModalButton extends AjaxButton{
	private static final long serialVersionUID = 1L;

	ModalWindow	modal;
	BasePage parent;
	
	public ResponseFormModalButton(String id, final ModalWindow modal, boolean visible) {
		super(id);
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

	@Override
	public void onError(AjaxRequestTarget target, Form<?> form) {
		System.out.println("Error???"); 
		System.out.println(target.getLogData().toString());
		return;
	}

}
