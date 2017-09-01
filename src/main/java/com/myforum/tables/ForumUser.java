package com.myforum.tables;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "FORUM_USER")
public class ForumUser extends AVKTable implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue
	@Column(name = "code", unique = true, nullable = false)
	private int code;

	@Column(name = "username")
	private String username;
	
	@Column(name = "password")
	private String password;

	@Column(name = "avatar")
	private byte[] avatar;
	
	@Column(name = "email_address")
	private String emailAddress;

	@Column(name = "display_name")
	private String displayName;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "code_classification", nullable=false, insertable=true, updatable=true)
	private Classification classification;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="forumUser")
	private Set<Message> messages = new HashSet<Message>(0);
	
	 
	public void setCode(int code)
	{
	    this.code = code;
	}
	public int getCode()
	{
	    return code;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUsername() {
		return username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return password;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public void setMessages(Set<Message> messages) {
		this.messages = messages;
	}
	
	public Set<Message> getMessages() {
		return messages;
	}

	public void setClassification(Classification classification) {
		this.classification = classification;
	}

	public Classification getClassification() {
		return classification;
	}
	public void setAvatar(byte[] imageData) {
		this.avatar = imageData;
	}
	public byte[] getAvatar() {
		return avatar;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getEmailAddress() {
		return emailAddress;
	}

	public String toString(){
		return getUsername();
	}

}
