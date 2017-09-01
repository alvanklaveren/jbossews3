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
@Table(name = "SOURCE_CATEGORY")
public class SourceCategory extends AVKTable implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue
	@Column(name = "code", unique = true, nullable = false)
	private int code;

	@Column(name = "description")
	private String description;

	@Column(name = "sortorder")
	private int sortorder;

	@OneToMany(fetch = FetchType.LAZY, mappedBy="sourceCategory")
	private Set<SourceSubject> sourceSubjects = new HashSet<SourceSubject>(0);
	
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
	
	public int getSort(){
		return sortorder;
	}

	public void setSourceSubjects(Set<SourceSubject> sourceSubjects) {
		this.sourceSubjects = sourceSubjects;
	}

	public Set<SourceSubject> getSourceSubjects() {
		return sourceSubjects;
	}
	
	public void addSourceSubject(SourceSubject sourceSubject){
		sourceSubjects.add(sourceSubject);
	}

	public void removeSourceSubject(SourceSubject sourceSubject) {
		sourceSubjects.remove(sourceSubject);
		
	}
	
	public void removeAllMessage() {
		sourceSubjects.removeAll( this.getSourceSubjects() );
		
	}
	
	public SourceCategory duplicate( ){
		SourceCategory sourceCategory = new SourceCategory();
		sourceCategory.setCode(this.getCode());
		sourceCategory.setDescription( this.getDescription());
		sourceCategory.setSourceSubjects( this.getSourceSubjects() );
		return sourceCategory;
	}
	
	public String toString(){
		return getDescription();
	}

	
}
