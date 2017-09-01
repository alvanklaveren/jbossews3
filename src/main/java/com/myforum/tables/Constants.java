package com.myforum.tables;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CONSTANTS")
public class Constants extends AVKTable implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "code", unique = true, nullable = false)
	private int code;

	@Column(name = "id")
	private String id;

	@Column(name = "string_value")
	private String stringValue;

	@Column(name = "blob_value")
	private byte[] blobValue;
	
	public void setCode(int code) {
		this.code = code;
	}
	public int getCode() {
		return code;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}
	public String getStringValue() {
		return stringValue;
	}
	public void setBlobValue(byte[] blobValue) {
		this.blobValue = blobValue;
	}
	public byte[] getBlobValue() {
		return blobValue;
	}
	public byte[] getConstantBlobValue(String string) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
