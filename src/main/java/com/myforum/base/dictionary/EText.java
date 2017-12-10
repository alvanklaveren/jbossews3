package com.myforum.base.dictionary;

public enum EText {

	  ADD_PRODUCT( "Add Product")
	, ADD_COMPANY( "Add Company" )
	, ADD_RATING_URL( "Add Rating URL" )
	, ADMINISTRATOR_MAINTENANCE( "Administrator Maintenance" )
	, FORGOT_PASSWORD( "Forgot password?" )
	, GAME_CONSOLE(	"Game Console")
	, INCORRECT_LOGIN_CREDENTIALS( "Incorrect login credentials" )
	, MOVE_MESSAGE( "Move Message To Other Category" )
	, NUMBER_OF_ITEMS( "Number of Items" )
	, PRODUCT_TYPE(	"Product Type")
	, RECENTLY_ADDED( "Most recently added products" )
	, REGISTER_NEW_USER( "Register New User" )
	, SEARCH_RESULTS( "Search Results" )
	, UPDATE_PROFILE( "Update Profile")
	, UPLOAD_FAILED( "Could not upload file. Check file name and size (< 100kb)" )
	;
	
	String text;
	
	EText(String text){
		this.text = text;
	}
	
	public String toString(){
		return text;
	}
}
