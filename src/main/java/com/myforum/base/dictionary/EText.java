package com.myforum.base.dictionary;

public enum EText {

	  ADD_CATEGORY( "Add Category" )
	, ADD_PRODUCT( "Add Product")
	, ADD_COMPANY( "Add Company" )
	, ADD_RATING_URL( "Add Rating URL" )
	, APPLY_CHANGES( "Apply Changes" )
	, ADMINISTRATOR_MAINTENANCE( "Administrator Maintenance" )
	, CIB_LABEL( "CIB = complete in box, but should be read as \"at least includes manual\"" ) 
	, DO_DELETE_CATEGORY( "Do you really want to delete this category?" )
	, FAILED_CHANGE_PASSWORD( "Failed to change your password. Sucks to be you." )
	, FAILED_GENERATE_PASSWORD( "Failed to generate a new password." )
	, FORGOT_PASSWORD( "Forgot password?" )
	, GAME_CONSOLE(	"Game Console")
	, INCORRECT_LOGIN_CREDENTIALS( "Incorrect login credentials" )
	, MOVE_MESSAGE( "Move Message To Other Category" )
	, NUMBER_OF_ITEMS( "Number of Items" )
	, PLEASE_ADD_URL( "Please add a URL" )
	, PLEASE_APPLY_RATING( "Please apply a rating" )
	, PRODUCT_TYPE(	"Product Type")
	, RECENTLY_ADDED( "Most recently added products" )
	, REGISTER_NEW_USER( "Register New User" )
	, SEARCH_RESULTS( "Search Results" )
	, UNABLE_SEND_EMAIL( "Unable to send email" )
	, UPDATE_PROFILE( "Update Profile")
	, UPLOAD_FAILED( "Could not upload file. Check file name and size (< 100kb)" )
	, ABOUT_WEBSITE ("About this website ..." )
	, ABOUT_WEBSITE_TEXT( "Alvanklaveren.com is created, written and maintained by A.L. van Klaveren. It is (currently) built " +  
					 	  "in Java (EE), using <a href=\"http://hibernate.org/\">Apache Hibernate</a>, <a href=\"http://wicket.apache.org/\">Apache Wicket</a> " +
					 	  "and <a href=\"https://spring.io/\">Spring Framework</a>. The website is running on <a href=\"http://tomcat.apache.org/\">Tomcat 8</a> " +
					 	  "on a hosting platform called <a href = \"https://www.digitalocean.com/\">Digital Ocean</a>. Last but not least, the website's layout is " +
					 	  "supported by <a href=\"http://getbootstrap.com/\"> Bootstrap</a>. <br><br> " +
					 	  "If you have a question or just want to contact the author, please leave a message at <a wicket:id=\"myemail\"></a>." 
				   )
	;
	

	String text;
	
	EText(String text){
		this.text = text;
	}
	
	public String toString(){
		return text;
	}
}
