package com.myforum.tables;



import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "MESSAGE")
public class Message extends AVKTable implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "code", unique = true, nullable = false)
	private int code;

	@Column(name = "description")
	private String description;
	
	@Column(name = "message_text")
	private String messageText;
	
	@Column(name = "message_date")
	private Date messageDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "code_category", nullable=false, insertable=true, updatable=true)
	private MessageCategory messageCategory;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="code_forum_user", nullable=false, insertable=true, updatable=true)
	private ForumUser forumUser;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="code_message", nullable=true, insertable=true, updatable=true)
	private Message message;

	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
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
	 
	public void setMessageCategory(MessageCategory messageCategory) {
		this.messageCategory = messageCategory;
	}
	
	public MessageCategory getMessageCategory() {
		return messageCategory;
	}
	public void setMessageDate(Date messageDate) {
		this.messageDate = messageDate;
	}
	public Date getMessageDate() {
		return messageDate;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
	public String getMessageText() {
		return messageText;
	}
	public void setForumUser(ForumUser forumUser) {
		this.forumUser = forumUser;
	}
	public ForumUser getForumUser() {
		return forumUser;
	}
	public String toString(){
		return getDescription();
	}
}
