package com.myforum.base.dictionary;

public enum EText {

	  RECENTLY_ADDED( "Most recently added products" )
	, SEARCH_RESULTS( "Search Results" )
	, UPLOAD_FAILED( "Could not upload file. Check file name and size (< 100kb)" )
	, ADD_PRODUCT( "Add Product")
	, ADD_COMPANY( "Add Company" )
	, ADD_RATING_URL( "Add Rating URL" )
	, REFRESH( "Refresh" )
	;
	
	String text;
	
	EText(String text){
		this.text = text;
	}
	
	public String toString(){
		return text;
	}
}
