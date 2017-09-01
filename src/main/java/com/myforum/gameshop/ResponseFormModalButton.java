package com.myforum.gameshop;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Button;

import com.myforum.base.BasePage;

public class ResponseFormModalButton extends Button{
	private static final long serialVersionUID = 1L;

	ModalWindow	modal;
	BasePage parent;
	
	public ResponseFormModalButton(String id, final ModalWindow modal, boolean visible) {
		super(id);
		this.modal = modal;
		setDefaultFormProcessing(false);
        setVisible(visible);
		add( new AjaxEventBehavior("onclick"){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onEvent(AjaxRequestTarget target) {
				modal.show(target);
				return;
			}
		});
	}

}
