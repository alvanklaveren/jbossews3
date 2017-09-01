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
@Table(name = "SOURCE_TEXT")
public class SourceText extends AVKTable implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "code", unique = true, nullable = false)
	private int code;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "code_source_subject", nullable=false, insertable=true, updatable=true)
	private SourceSubject sourceSubject;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "code_source_type", nullable=false, insertable=true, updatable=true)
	private SourceType sourceType;

	@Column(name = "html_document")
	private String htmlDocument;
	
	public void setCode(int code)
	{
	    this.code = code;
	}

	public int getCode()
	{
	    return code;
	}
	
	public void setSourceSubject(SourceSubject sourceSubject) {
		this.sourceSubject = sourceSubject;
	}
	
	public SourceSubject getSourceSubject() {
		return sourceSubject;
	}

	public void setSourceType(SourceType sourceType) {
		this.sourceType = sourceType;
	}
	
	public SourceType getSourceType() {
		return sourceType;
	}

	public void setHTMLDocument(String htmlDocument) {
		this.htmlDocument = htmlDocument;
	}
	public String getHTMLDocument() {
		return htmlDocument;
	}

}
