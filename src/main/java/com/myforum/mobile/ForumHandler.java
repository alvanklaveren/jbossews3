package com.myforum.mobile;

public class ForumHandler //extends AbstractHandler
{
//	private String 	forumName = AllConstants.getForumName();
//	private Session session;
//	
//	private final String LOGIN 				= "login"; 
//	private final String LOGOUT 			= "logout"; 
//	private final String RESETDBHELPER 		= "resetdbhelper"; 
//	private final String GETCATEGORIES 		= "getcategories"; 
//	private final String GETMESSAGES 		= "getmessages"; 
//	private final String GETMESSAGE 		= "getmessage"; 
//	private final String GETFORUMNAME 		= "getforumname"; 
//	private final String GETAVATAR 			= "getavatar"; 
//	private final String ADDMESSAGE 		= "addmessage"; 
//	private final String CHECKNOTIFICATIONS = "check_notifications"; 
//	
//	private final MobileUtils utils = new MobileUtils();
//
//    public ForumHandler(){}
// 
//    @Override
//	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
//    { 
//    	response.setContentType("text/html;charset=utf-8");
//        response.setStatus(HttpServletResponse.SC_OK);
//        baseRequest.setHandled(true);  
//  	
//        String parmValue 			= request.getParameter("action");
//        String codeMessageCategory 	= request.getParameter("codemessagecategory");
//        String codeMessage 			= request.getParameter("codemessage");
//        String codeForumUser		= request.getParameter("codeforumuser");
//        String userName				= request.getParameter("username");
//        String password				= request.getParameter("password");
//        String description			= request.getParameter("description");
//        String messageText			= request.getParameter("messagetext");
//        String macAddress			= request.getParameter("macaddress");
//        //String isAdministrator		= request.getParameter("isadministrator");
//         
//        if( parmValue.equals( RESETDBHELPER ) ){
//        	DBHelper.rebuildSessionFactory();       	
//        	response.getWriter().println( "DBHelper reset" ); 
//        }
//        
//        if( parmValue.equals( GETCATEGORIES ) ){
//        	response.getWriter().println( utils.getXMLMessageCategories() ); 
//        }
//        
//        if( parmValue.equals( GETMESSAGES ) ){
//        	response.getWriter().println( utils.getXMLMessages( Integer.valueOf(codeMessageCategory) ) ); 
//        }
//        
//        if( parmValue.equals( GETMESSAGE ) ){
//        	response.getWriter().println( utils.getXMLMessage( Integer.valueOf( codeMessage ) ) ); 
//        }
//        
//        if( parmValue.equals( GETFORUMNAME ) ){
//        	response.getWriter().println( forumName );         	
//        }
//        
//        if( parmValue.equals( GETAVATAR ) ){
//       		int code = Integer.parseInt( codeForumUser );       	
//        	response.getWriter().println( utils.getAvatarAsStringByCode( code ) );  
//        }
//        
//        if( parmValue.equals( LOGIN ) ){
//        	LoginCred loginCred;
//       	
//        	codeForumUser = String.valueOf( utils.checkLogin(userName, password) );
//        	
//        	if( codeForumUser.equals("0") ) {
//        		// session.get() will return a null if no rows found. Load would throw an exception
//        		response.getWriter().println("<logindata><codeforumuser>0</codeforumuser><errormessage>Incorrect combination of username and password.</errormessage></logindata>");
//            	
//            	if( macAddress == null ){ return; }
//        		loginCred = (LoginCred) session.get(LoginCred.class, macAddress);
//
//            	if(loginCred ==  null ){
//            		// no record found. Create new one
//            		loginCred = new LoginCred();
//            		loginCred.setMacAddress(macAddress);
//            	}
//            	loginCred.setCodeForumUser(0);
//            	loginCred.setUsername("");
//            	loginCred.setPassword("");
//            	
//        	}else{
//        		response.getWriter().println( "<logindata><codeforumuser>" + codeForumUser + "</codeforumuser><errormessage>" + userName + " logged in succesful</errormessage></logindata>");
//        		if(macAddress == null){ return; }
//        		
//        		session = DBHelper.openSession();
//            	loginCred = (LoginCred) session.get(LoginCred.class, macAddress);
//            	session.close();
//        		
//            	if(loginCred ==  null ){
//            		// no record found. Create new one
//            		loginCred = new LoginCred();
//            		loginCred.setMacAddress(macAddress);
//            	}
//            	loginCred.setCodeForumUser(Integer.parseInt( codeForumUser ));
//            	loginCred.setUsername(userName);
//            	loginCred.setPassword(password); // already MD5 encrypted
//        	}      	
//        	
//        	DBHelper.saveAndCommit( loginCred );
//        }
//
//        if( parmValue.equals( LOGOUT ) ){
//        	LoginCred logoutCred;
//        
//        	if( macAddress == null || macAddress.equals( "" ) ){ return; }
//        	
//        	session = DBHelper.openSession();        	
//        	// a valid mac-address is able to logout
//        	logoutCred = (LoginCred) session.get( LoginCred.class, macAddress );
//    		session.close();
//
//    		if(logoutCred !=  null ){
//        		// record found. update logout.
//            	logoutCred.setCodeForumUser( 0 );
//            	logoutCred.setUsername( "" );
//            	logoutCred.setPassword( "" );
//        	}
//        }
//
//        if( parmValue.equals( CHECKNOTIFICATIONS ) ){
//        	session = DBHelper.openSession(); 
//        	LoginCred notificationCred = (LoginCred) session.get(LoginCred.class, macAddress);    			
//    		session.close();
//    		
//	      	if( notificationCred ==  null || notificationCred.getCodeForumUser() == 0 ){ return; } // no record of user's macaddress found for auto login and notifications or user is logged off.
//	    	
//	    	String xmlString = utils.getNotifications( notificationCred );
//			response.getWriter().println( xmlString );
//        }
//
//        if( parmValue.equals( ADDMESSAGE ) ){
//        	int codeFU;
//        	try{
//        		 codeFU = Integer.parseInt(codeForumUser);
//        	}catch (NumberFormatException e)
//        	{
//        		codeFU=0;
//        	}
//        	if( codeFU == 0 ){
//        		response.getWriter().println( "No user logged in" );
//        	}else{
//            	if( description == null || description.equals("") ){
//            		response.getWriter().println( "Please enter a description." );
//            		return;
//            	}
//
//            	if( messageText == null || messageText.equals("")){
//            		response.getWriter().println( "Please enter a message." );
//            		return;
//            	}
//
//            	if( session.isOpen() ){ session.close(); }
//            	
//        		Message resultMessage;       	
//	        	resultMessage = utils.addMessage( Integer.parseInt(codeMessageCategory), Integer.parseInt(codeMessage), description, messageText, codeFU );
//	        	if (resultMessage == null){
//	        		response.getWriter().println("Saving message failed.");
//	        	}else{        		
//	        		response.getWriter().println("Saving message succesful");
//	        	}
//        	}        	
//        }
//
//    }
}
