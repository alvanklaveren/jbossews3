package com.myforum.models;

import org.apache.wicket.model.PropertyModel;

import com.myforum.tables.Message;

public class MessageModel extends PropertyModel<Message>{
	private static final long serialVersionUID = 1L;

	public MessageModel(Message message, String expression) {
		super(message, expression);

	}
	

}
