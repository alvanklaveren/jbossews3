package com.myforum.dictionary;

public enum EText {

	  ADD_CATEGORY( "Add Category" )
	, ADD_PRODUCT( "Add Product")
	, ADD_COMPANY( "Add Company" )
	, ADD_RATING_URL( "Add Rating URL" )
	, COMPANY_ALREADY_EXISTS( "Company already exists in the database")
	, APPLY_CHANGES( "Apply Changes" )
	, ADMINISTRATOR_MAINTENANCE( "Administrator Maintenance" )
	, CIB_LABEL( "CIB = complete in box, but should be read as \"at least includes manual\"" ) 
	, DO_DELETE_CATEGORY( "Do you really want to delete this category?" )
	, EMAIL_SENT( "Email sent" )
	, FAILED_CHANGE_PASSWORD( "Failed to change your password. Sucks to be you." )
	, FAILED_GENERATE_PASSWORD( "Failed to generate a new password." )
	, FORGOT_PASSWORD( "Forgot password?" )
	, GAME_CONSOLE(	"Game Console")
	, INCORRECT_LOGIN_CREDENTIALS( "Incorrect login credentials" )
	, MOVE_MESSAGE( "Move Message To Other Category" )
	, NUMBER_OF_ITEMS( "Number of Items" )
	, PLEASE_ADD_COMPANY( "Company description field is empty. Please add a company description.")
	, PLEASE_ADD_URL( "URL field is empty. Please add a valid URL." )
	, PLEASE_APPLY_RATING( "Please apply a rating" )
	, PRODUCT_TYPE(	"Product Type")
	, RECENTLY_ADDED( "Most recently added products" )
	, REGISTER_NEW_USER( "Register New User" )
	, SEARCH_RESULTS( "Search Results" )
	, SORT_BY( "Sort by" )
	, UNABLE_SEND_EMAIL( "Unable to send email" )
	, UPDATE_PROFILE( "Update Profile")
	, UPLOAD_FAILED( "Could not upload file. Check file name and size (< 100kb)" )
	, ABOUT_WEBSITE ("About ..." )
	, ABOUT_WEBSITE_TEXT( "<Translation not found>" )
	, ABOUT_ME ("About me")
	, CLICK_TO_NAVIGATE ("Click to navigate")
	, NUMBER_OF_MESSAGES ("# Messages")
	;
	

	String text;
	
	EText(String text){
		this.text = text;
	}
	
	public String toString(){
		return text;
	}
}
