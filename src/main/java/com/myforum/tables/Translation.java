package com.myforum.tables;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TRANSLATION")
public class Translation extends AVKTable implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "code", unique = true, nullable = false)
	private int code;

	@Column(name = "original")
	private String original;

	@Column(name = "us")
	private String us;

	@Column(name = "nl")
	private String nl;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	public String getUs() {
		return us;
	}

	public void setUs(String us) {
		this.us = us;
	}

	public String getNl() {
		return nl;
	}

	public void setNl(String nl) {
		this.nl = nl;
	}

	
}
