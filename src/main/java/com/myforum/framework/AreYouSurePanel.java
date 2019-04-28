package com.myforum.framework;

import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.myforum.dictionary.Translator;
 
 
public abstract class AreYouSurePanel extends Panel {
	private static final long serialVersionUID = 1L;
	
	protected ModalWindow confirmModal;
    protected AnswerPopup answerPopup;
    protected Map<String,String> modifiersToApply;
 
 
    public AreYouSurePanel(String id, String buttonName, String modalMessageText) {
        super( id );
        
        answerPopup = new AnswerPopup( "n" );
        addElements( id, buttonName, modalMessageText );      
    }
 
    protected void addElements( String id, String buttonName, String modalMessageText ) {
 
        confirmModal = createConfirmModal( id, Translator.getInstance().translate(modalMessageText) );
 
        StatelessForm<Object> form = new StatelessForm<Object>( "confirmForm" );
        add(form);
 
        AjaxButton confirmButton = new AjaxButton( "confirmButton", new Model<String>( Translator.getInstance().translate(buttonName) ) ) {

			private static final long serialVersionUID = 1L;

			@Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                confirmModal.show(target);
            }
        };
 
        form.add(confirmButton);
 
 
        form.add(confirmModal);
 
    }
 
    protected abstract void onConfirm(AjaxRequestTarget target);
    protected abstract void onCancel(AjaxRequestTarget target);
 
    protected ModalWindow createConfirmModal(String id, String modalMessageText) {
 
        ModalWindow modalWindow = new ModalWindow( "modal" );
        // modalWindow.setCookieName( id ); // Do not store x-y width-height in cookie. Modal window uses autosize instead.
        modalWindow.setAutoSize( true );
        modalWindow.setContent( new YesNoPanel( modalWindow.getContentId(), modalMessageText, modalWindow, answerPopup ) );
        modalWindow.setWindowClosedCallback( new ModalWindow.WindowClosedCallback() {

			private static final long serialVersionUID = 1L;

			@Override
            public void onClose( AjaxRequestTarget target ) {
                if ( answerPopup.getAnswer() == "y" ) {
                    onConfirm( target );
                } else {
                    onCancel( target );
                }
            }
        });
 
        return modalWindow;
    }
 
 
}