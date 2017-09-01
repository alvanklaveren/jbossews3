package com.myforum.mobile;

import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.myforum.application.AllConstants;
import com.myforum.application.DBHelper;
import com.myforum.application.XmlElement;
import com.myforum.tables.ForumUser;
import com.myforum.tables.LoginCred;
import com.myforum.tables.Message;
import com.myforum.tables.MessageCategory;
import com.myforum.tables.Notifications;
import com.myforum.tables.dao.ForumUserDao;
import com.myforum.tables.dao.MessageCategoryDao;
import com.myforum.tables.dao.MessageDao;
import com.myforum.tables.dao.NotificationsDao;

public class MobileUtils{
	
	public MobileUtils(){}
		
	public  String getXMLMessageCategories() {
		MessageCategoryDao 		messageCategoryDao 	= new MessageCategoryDao();
		List<MessageCategory> 	messageCategoryList = messageCategoryDao.list();
		
		XmlElement xml = new XmlElement( "category", "" );		
		
		for( MessageCategory messageCategory: messageCategoryList ){
			xml.addChild( new XmlElement( "code", messageCategory.getCode() ) );
			xml.addChild( new XmlElement( "description", messageCategory.getDescription() ) );
			xml.addChild( new XmlElement( "messagecount", messageCategory.getMessages().size() ) );			
		}			

		return xml.toString();
    }
	
	public  String getXMLMessages( int codeMessageCategory ) {
		MessageDao 			messageDao 			= new MessageDao();
		MessageCategoryDao 	messageCategoryDao 	= new MessageCategoryDao();
		StringBuilder 		XMLStringBuilder	= new StringBuilder();   

	    List<Message> messageList 	= messageDao.getMessages( messageCategoryDao.find(codeMessageCategory) ); 

		for( Message message : messageList ){
			XmlElement xml = new XmlElement( "message", "" );
			
			xml.addChild( new XmlElement( "code", message.getCode() ) );
			xml.addChild( new XmlElement( "description", message.getDescription() ) );
			xml.addChild( new XmlElement( "date", message.getMessageDate() ) );
			xml.addChild( new XmlElement( "username", message.getForumUser().getUsername() ) );
			xml.addChild( new XmlElement( "codeforumuser", message.getCode() ) );

			XMLStringBuilder.append( xml.toString() );			
		}
				
		return XMLStringBuilder.toString();
    }

	public  String getXMLMessage( int codeMessage ) {
		MessageDao messageDao 		= new MessageDao();
		Message message 			= messageDao.find( codeMessage );

		int		codeForumUser				= message.getForumUser().getCode();
		int		codeMessageCategory 		= message.getMessageCategory().getCode();
		String 	messageDescription 			= filterText( message.getDescription() );
		Date	messageDate 				= message.getMessageDate();
		String	username					= message.getForumUser().getUsername();
		String	messageText					= encodeString( message.getMessageText() );
		String	messageCategoryDescription 	= message.getMessageCategory().getDescription();
		
		XmlElement xml = new XmlElement( "message", "" );
		
		xml.addChild( new XmlElement( "code", 							codeMessage ) );
		xml.addChild( new XmlElement( "description", 					messageDescription ) );
		xml.addChild( new XmlElement( "date", 							messageDate ) );
		xml.addChild( new XmlElement( "username", 						username ) );
		xml.addChild( new XmlElement( "codeforumuser", 					codeForumUser ) );
		xml.addChild( new XmlElement( "messagetext", 					messageText ) );
		xml.addChild( new XmlElement( "codemessagecategory", 			codeMessageCategory ) );
		xml.addChild( new XmlElement( "messagecategorydescription", 	messageCategoryDescription ) );
		xml.addChild( new XmlElement( "avatar", 						getAvatarAsStringByCode( codeForumUser ) ) );

		List<Message> messageThreadList = messageDao.getThreadMessages( message );

		for( Message threadMessage:messageThreadList){
			ForumUser 	threadForumUser 	= threadMessage.getForumUser();
			int 		threadCodeForumUser	= threadForumUser.getCode();
			String		threadUsername		= threadForumUser.getUsername();	

			int		threadCodeMessage		 			= threadMessage.getCode();
			String 	threadMessageDescription 			= filterText( threadMessage.getDescription() );
			Date	threadMessageDate 					= threadMessage.getMessageDate();
			String	threadMessageText					= encodeString( threadMessage.getMessageText() );

			XmlElement threadXml = new XmlElement( "threadmessage", "" );
			
			threadXml.addChild( new XmlElement( "threadcode", 			threadCodeMessage ) );
			threadXml.addChild( new XmlElement( "threaddescription", 	threadMessageDescription ) );
			threadXml.addChild( new XmlElement( "threaddate", 			threadMessageDate ) );
			threadXml.addChild( new XmlElement( "threadcodeforumuser", 	threadCodeForumUser ) );
			threadXml.addChild( new XmlElement( "threadusername", 		threadUsername ) );
			threadXml.addChild( new XmlElement( "threadmessagetext", 	threadMessageText ) );
			
			xml.addChild( threadXml );
		}
		
		return xml.toString();
    }
	
	public  String getNotifications( LoginCred loginCred ){
		NotificationsDao notificationsDao 		= new NotificationsDao();    	    
		MessageDao messageDao 					= new MessageDao();	    	    
    	
    	List<Notifications> notificationsList 	= notificationsDao.find( loginCred.getForumUser() , true /*only the ones that have not been sent yet*/ );

		XmlElement xml = new XmlElement( "notification", "" );

   	   	for ( Notifications notifications : notificationsList ){
    		Message 		message 		= messageDao.find( notifications.getMessage().getCode() );
    		MessageCategory messageCategory = message.getMessageCategory();  		
    		
    		xml.addChild( new XmlElement( "codeforumuser", 				notifications.getForumUser().getCode() ) );
    		xml.addChild( new XmlElement( "codemessage", 				notifications.getMessage().getCode() ) );
    		xml.addChild( new XmlElement( "codethreadmessage", 			notifications.getThreadMessage().getCode() ) );
    		xml.addChild( new XmlElement( "messagedescription", 		notifications.getMessageDescription() ) );
    		xml.addChild( new XmlElement( "threadmessagedescription", 	notifications.getThreadMessageDescription() ) );
    		xml.addChild( new XmlElement( "threadusername", 			notifications.getThreadUsername() ) );
    		xml.addChild( new XmlElement( "messagecategorydescription", messageCategory.getDescription() ) );
    		        				
    		// and reset notification
    		notifications.setMustSend( "n" );
    		
        	notificationsDao.update( notifications );
    	}
		
		return xml.toString();
	}
	
	public  String filterText( String text ){
		// several characters are not allowed in xml parser
		// & - &amp; 
		// < - &lt; 
		// > - &gt; 
		// " - &quot; 
		// ' - &#39; 

		//text = text.replaceAll("&", "&amp");
		//text = text.replaceAll("'", "&#39");
		text = text.replaceAll("<", "[");
		text = text.replaceAll(">", "]");
		return text;
	}
    	
	public  String getAvatarAsStringByCode( int codeForumUser ){
		ForumUserDao forumUserDao 	= new ForumUserDao();
		ForumUser forumUser = forumUserDao.find( codeForumUser );
		
		byte[] image = forumUser.getAvatar();
		if( image == null || image.length == 0 ) { 
			image = AllConstants.getConstantBlobValue("guestimage"); 
		}
			
		return new String( new Base64().encode( image ) );
	}
	
	public  Message addMessage( int codeCategory, int codeMessage, String description, String messageText, int codeForumUser ){
		if( codeForumUser == 0 ){ return null; }	
		if( description == null || description.equals("") || messageText == null || messageText.equals("")){ return null; }

		// implement saving message here before retrieving page again
    	Message replyMessage = new Message();
    	
    	replyMessage.setMessageCategory(new MessageCategoryDao().find(codeCategory));  	
    	replyMessage.setMessage( new MessageDao().find(codeMessage) ); // is this the original message or a reply to a message depends on value of codeMessage
    	replyMessage.setMessageDate( new Date() );
		replyMessage.setForumUser( new ForumUserDao().find(codeForumUser) );
    	replyMessage.setDescription( description );
    	replyMessage.setMessageText( messageText );
    	
    	MessageDao messageDao = new MessageDao();
    	messageDao.add( replyMessage );
    	 
    	// add reply to notification list to notify user of original message
    	if( codeMessage != 0 ){
    		Message originalMessage = messageDao.find( codeMessage );
    		
    		ForumUserDao forumUserDao			= new ForumUserDao();
	    	NotificationsDao notificationsDao 	= new NotificationsDao();
	    	Notifications notifications 		= new Notifications();
    		ForumUser forumUser					= forumUserDao.find( codeForumUser );
	    	
	    	notifications.setForumUser( originalMessage.getForumUser() ); // user of original message that will be notified
	    	notifications.setMessage( originalMessage );
	    	notifications.setMessageDescription( originalMessage.getDescription() );
	    	notifications.setThreadMessage( replyMessage );
	    	notifications.setThreadUsername( forumUser.getUsername() );
	    	notifications.setThreadMessageDescription( description );
	    	notifications.setMustSend("y");
	    	
	    	notificationsDao.add( notifications );
    	}
    	
    	return replyMessage;
    
	}
	
	public  String encodeString( String string ){
		byte[] bytesEncoded = Base64.encodeBase64(string.getBytes());
		string = new String( bytesEncoded );
		return string;
	}
	
	public  int checkLogin( String username, String password ){
		int forumUserCode = 0;
		Session session = DBHelper.openSession();
		Criteria criteria = session.createCriteria(ForumUser.class);
		
		criteria.add(Restrictions.eq("username", username ));
		if( criteria.list().size() > 0 ){
			ForumUser forumUser = (ForumUser) criteria.list().get(0);
			if( forumUser.getPassword().equals(password) ){
				forumUserCode = forumUser.getCode();
			}
		}
		
		session.close();
		return forumUserCode;	
	}

}

