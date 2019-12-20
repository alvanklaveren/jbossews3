package com.myforum.tables;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "MESSAGE_IMAGE")
public class MessageImage extends AVKTable implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue
	@Column(name = "code", unique = true, nullable = false)
	private int code;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "code_message", nullable=true, insertable=true, updatable=true)
	private Message message;

	@Column(name = "image")
	private byte[] image;

	@Column(name = "sortorder")
	private int sortorder;
	
	public void setCode(int code) {
		this.code = code;
	}
	public int getCode() {
		return code;
	}

	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}

	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public int getSortorder() {
		return sortorder;
	}
	public void setSortorder(int sortorder) {
		this.sortorder = sortorder;
	}

	public String toString(){
		return "image";
	}

}
