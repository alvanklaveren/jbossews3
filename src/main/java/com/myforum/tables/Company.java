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
@Table(name = "COMPANY")
public class Company extends AVKTable implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue
	@Column(name = "code", unique = true, nullable = false)
	private int code;

	@Column(name = "description")
	private String description;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="company")
	private Set<GameConsole> gameConsoles = new HashSet<GameConsole>(0);
	
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
	
	public void setGameConsoles(Set<GameConsole> gameConsoles) {
		this.gameConsoles = gameConsoles;
	}
	public Set<GameConsole> getGameConsoles() {
		return gameConsoles;
	}
	
	public String toString(){
		return getDescription();
	}

}
