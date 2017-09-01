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
@Table(name = "CLASSIFICATION")
public class Classification extends AVKTable implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue
	@Column(name = "code", unique = true, nullable = false)
	private int code;

	@Column(name = "description")
	private String description;
	
	@Column(name = "is_admin")
	private int isAdmin;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="classification")
	private Set<ForumUser> forumUsers = new HashSet<ForumUser>(0);
	
	public void setCode(int code) {
		this.code = code;
	}
	public int getCode() {
		return code;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}
	public void setIsAdmin(int isAdmin) {
		this.isAdmin = isAdmin;
	}
	public int getIsAdmin() {
		return isAdmin;
	}
	
	public int isAdmin(){
		return isAdmin;
	}
	
	public void setForumUsers(Set<ForumUser> forumUsers) {
		this.forumUsers = forumUsers;
	}
	public Set<ForumUser> getForumUsers() {
		return forumUsers;
	}
	
	public String toString(){
		return getDescription();
	}

}
