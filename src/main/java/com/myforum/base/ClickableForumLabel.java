package com.myforum.base;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import com.myforum.application.CookieLogics;
import com.myforum.forumpages.ForumBasePage;
import com.myforum.forumpages.ForumCategoryPanel;
import com.myforum.forumpages.ForumMessagePanel;
import com.myforum.tables.Message;
import com.myforum.tables.MessageCategory;

public class ClickableForumLabel extends Label{
	private static final long serialVersionUID = 1L;

	private ForumBasePage 	parent;
	private MessageCategory messageCategory;
	private Message			message;
	
	public ClickableForumLabel(String id, Model<String> description) {
		super(id, description);
		setOutputMarkupId(true); // allows you to change text of this component dynamically
		addClick();
	}
	
	public void setParent(ForumBasePage parent){
		this.parent = parent;
	}
	
	public void setMessageCategory(MessageCategory messageCategory){
		this.messageCategory = messageCategory;
	}

	public void setMessage(Message message){
		this.message = message;
	}

	private void addClick(){
			add( new AjaxEventBehavior( "onclick") {
			private static final long serialVersionUID = 1L;
			@Override
	        protected void onEvent(AjaxRequestTarget target) {
				if(message != null){
					CookieLogics.setCookieInt( "codeMessage", message.getCode() );
					target.add( new ForumMessagePanel(parent) );
					return;
				}
				if(messageCategory != null){
					CookieLogics.setCookieInt( "codeMessageCategory", messageCategory.getCode() );
					target.add( new ForumCategoryPanel(parent) );
					return;
				}
	        }
		});
	}	
}
