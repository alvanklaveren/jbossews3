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
@Table(name = "SOURCE_TYPE")
public class SourceType extends AVKTable implements Serializable, Comparable<SourceType> {
	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue
	@Column(name = "code", unique = true, nullable = false)
	private int code;

	@Column(name = "description")
	private String description;

	@Column(name = "sortorder")
	private int sortorder;

	@OneToMany(fetch = FetchType.LAZY, mappedBy="sourceType")
	private Set<SourceText> sourceTexts = new HashSet<SourceText>(0);
	
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
	
	public void setSortOrder( int sortorder ){
		this.sortorder = sortorder;
	}
	
	public int getSortorder(){
		return sortorder;
	}

	public void setSourceTexts(Set<SourceText> sourceTexts) {
		this.sourceTexts = sourceTexts;
	}

	public Set<SourceText> getSourceTexts() {
		return sourceTexts;
	}
	
	public void addSourceSubject(SourceText sourceText){
		sourceTexts.add(sourceText);
	}

	public void removeSourceSubject(SourceText sourceText) {
		sourceTexts.remove(sourceText);
		
	}
	
	public void removeAllMessage() {
		sourceTexts.removeAll( this.getSourceTexts() );
		
	}
	
	public SourceType duplicate(){
		SourceType sourceType = new SourceType();
		sourceType.setCode(this.getCode());
		sourceType.setDescription( this.getDescription());
		sourceType.setSourceTexts( this.getSourceTexts() );
		return sourceType;
	}
	
	@Override
	public String toString(){
		return getDescription();
	}

	@Override
	public int compareTo(SourceType sourceType ) {
		return this.sortorder - sourceType.getSortorder();
	}


	
}
