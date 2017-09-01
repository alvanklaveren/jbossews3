package com.myforum.tables;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RATING_URL")
public class RatingUrl extends AVKTable implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "code", unique = true, nullable = false)
	private int code;

	@Column(name = "url")
	private String url;

	
	public void setCode(int code){
	    this.code = code;
	}

	public int getCode(){
	    return code;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}

	public String toString(){
		return getUrl();
	}


}
