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
@Table(name = "SOURCE_SUBJECT")
public class SourceSubject extends AVKTable implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "code", unique = true, nullable = false)
	private int code;

	@Column(name = "description")
	private String description;
	
	@Column(name = "subject_text")
	private String subjectText;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="sourceSubject")
	private Set<SourceText> sourceTexts = new HashSet<SourceText>(0);
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "code_source_category", nullable=false, insertable=true, updatable=true)
	private SourceCategory sourceCategory;
	
	@Column(name = "sortorder")
	private int sortorder;

	public void setCode(int code)
	{
	    this.code = code;
	}

	public int getCode()
	{
	    return code;
	}
	 
	public void setDescription(String description)
	{
	    this.description = description;
	}
	public String getDescription()
	{
	    return description;
	}
	 
	public void setSourceCategory(SourceCategory sourceCategory) {
		this.sourceCategory = sourceCategory;
	}
	
	public SourceCategory getSourceCategory() {
		return sourceCategory;
	}
	
	public void setSubjectText(String subjectText) {
		this.subjectText = subjectText;
	}
	public String getSubjectText() {
		return subjectText;
	}

	public void setSortOrder( int sortorder ){
		this.sortorder = sortorder;
	}

	public int getSortorder(){
		return sortorder;
	}

	public void setSourceTexts( Set<SourceText> sourceTexts ) {
		this.sourceTexts = sourceTexts;
	}

	public Set<SourceText> getSourceTexts() {
		return sourceTexts;
	}
	
	public void addSourceText( SourceText sourceText ){
		sourceTexts.add( sourceText );
	}

	public void removeSourceText(SourceText sourceText) {
		sourceTexts.remove(sourceText);
		
	}
	
	public void removeAllMessage() {
		sourceTexts.removeAll( getSourceTexts() );
		
	}
	
	public String toString(){
		return getDescription();
	}
}
