package com.myforum.tables;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "LOGIN_CRED")
public class LoginCred extends AVKTable implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "mac_address", unique = true, nullable = false)
	private String macAddress;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="code_forum_user", nullable=false, insertable=true, updatable=true)
	private ForumUser forumUser;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "password")
	private String password;

	@Column(name = "magic_token")
	private String magicToken;
	
	@Column(name = "token_date")
	private Date tokenDate;
	
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	public String getMacAddress() {
		return macAddress;
	}

	public ForumUser getForumUser() {
		return forumUser;
	}
	public void setForumUser(ForumUser forumUser) {
		this.forumUser = forumUser;
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
	public String getMagicToken() {
		return magicToken;
	}
	public void setMagicToken(String magicToken) {
		this.magicToken = magicToken;
	}
	public void setTokenDate(Date tokenDate){
		this.tokenDate = tokenDate;
	}
	public Date getTokenDate(){
		return tokenDate;
	}

	@Override
	public int getCode() {
		return hashCode();
	}
}
