package com.myforum.framework;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.myforum.dictionary.Translator;
 
public class YesNoPanel extends Panel {

	private static final long serialVersionUID = 1L;

	public YesNoPanel( String id, String message, final ModalWindow modalWindow, final AnswerPopup answerPopup ) {
        super( id );
 
        Form<Object> yesNoForm = new Form<Object>( "yesNoForm" );
 
        MultiLineLabel messageLabel = new MultiLineLabel( "message", message );
        yesNoForm.add(messageLabel);
        modalWindow.setTitle( "Please confirm" );
        modalWindow.setInitialHeight( 200 );
        modalWindow.setInitialWidth( 350 );
 
        AjaxButton yesButton = new AjaxButton( "yesButton", new Model<String>(Translator.getInstance().translate("Yes")), yesNoForm ) {

			private static final long serialVersionUID = 1L;

			@Override
            protected void onSubmit( AjaxRequestTarget target, Form<?> form ) {
                if ( target != null ) {
                	answerPopup.setAnswer( "y" );
                    modalWindow.close( target );
                }
            }
        };
 
        AjaxButton noButton = new AjaxButton( "noButton", new Model<String>(Translator.getInstance().translate("No")), yesNoForm ) {
 
			private static final long serialVersionUID = 1L;

			@Override
            protected void onSubmit( AjaxRequestTarget target, Form<?> form ) {
                if (target != null) {
                	answerPopup.setAnswer( "n" );
                    modalWindow.close( target );
                }
            }
        };
 
        yesNoForm.add( yesButton );
        yesNoForm.add( noButton );
 
        add( yesNoForm );
        
    }
 
}