package com.myforum.tables;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "NOTIFICATIONS")
@IdClass(Notifications.class)
public class Notifications extends AVKTable implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="code_forum_user", nullable=false, insertable=true, updatable=true)
	private ForumUser forumUser;

	@Id
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="code_message", nullable=false, insertable=true, updatable=true)
	private Message message;

	@Id
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="code_thread_message", nullable=false, insertable=true, updatable=true)
	private Message threadMessage;

	@Column(name = "message_description")
	private String 	messageDescription;

	@Column(name = "thread_message_description")
	private String 	threadMessageDescription;

	@Column(name = "thread_username")
	private String 	threadUsername;
	
	@Column(name = "must_send")
	private String 	mustSend;
	
	public String toString(){
		return getMessageDescription();
	}

	public ForumUser getForumUser() {
		return forumUser;
	}


	public void setForumUser(ForumUser forumUser) {
		this.forumUser = forumUser;
	}


	public Message getMessage() {
		return message;
	}


	public void setMessage(Message message) {
		this.message = message;
	}


	public Message getThreadMessage() {
		return threadMessage;
	}


	public void setThreadMessage(Message threadMessage) {
		this.threadMessage = threadMessage;
	}

	public void setMessageDescription(String messageDescription) {
		this.messageDescription = messageDescription;
	}


	public String getMessageDescription() {
		return messageDescription;
	}


	public void setThreadUsername(String threadUsername) {
		this.threadUsername = threadUsername;
	}


	public String getThreadUsername() {
		return threadUsername;
	}


	public void setMustSend(String mustSend) {
		this.mustSend = mustSend;
	}


	public String getMustSend() {
		return mustSend;
	}


	public void setThreadMessageDescription(String threadMessageDescription) {
		this.threadMessageDescription = threadMessageDescription;
	}


	public String getThreadMessageDescription() {
		return threadMessageDescription;
	}
	
	@Override
	public boolean equals( Object other ){
		return( this == other );
	}
	
	@Override
	public int hashCode(){
		return 100000 * forumUser.getCode() + 1000 * message.getCode() + 1 * threadMessage.getCode();
	}

	@Override
	public int getCode() {
		return hashCode();
	}

}
