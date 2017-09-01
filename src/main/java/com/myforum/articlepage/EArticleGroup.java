package com.myforum.articlepage;

public enum EArticleGroup {

	INDEX	(0, ""),
	JAVA	(1, "JAVA"),
	WEB		(2, "Web related"),
	Python  (3, "Python")
	;
	
	int 	sortorder;
	String 	groupDescription;
	
	EArticleGroup(int sortorder, String groupDescription){
		this.sortorder 			= sortorder;
		this.groupDescription 	= groupDescription;
	}
	
	public int getSortorder(){
		return sortorder;
	}
	
	public String getDescription(){
		return groupDescription;
	}
}
