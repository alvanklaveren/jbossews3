package com.myforum.tables;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "MESSAGE_CATEGORY")
public class MessageCategory extends AVKTable implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue
	@Column(name = "code", unique = true, nullable = false)
	private int code;

	@Column(name = "description")
	private String description;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="messageCategory")
	private Set<Message> messages = new HashSet<Message>(0);
	
	public void setCode(int code)
	{
	    this.code = code;
	}
	public int getCode()
	{
	    return code;
	}
	 
	public void setDescription(String description)
	{
	    this.description = description;
	}
	public String getDescription()
	{
	    return description;
	}
	public void setMessages(Set<Message> messages) {
		this.messages = messages;
	}

	public Set<Message> getMessages() {
		return messages;
	}
	
	public void addMessage(Message message){
		messages.add(message);
	}

	public void removeMessage(Message message) {
		messages.remove(message);
		
	}
	
	public void removeAllMessage() {
		messages.removeAll( this.getMessages() );
		
	}
	public MessageCategory duplicate(){
		MessageCategory messageCategory = new MessageCategory();
		messageCategory.setCode(this.getCode());
		messageCategory.setDescription(this.getDescription());
		messageCategory.setMessages(this.getMessages());
		return messageCategory;
	}
	
	public String toString(){
		return getDescription();
	}

	
}
