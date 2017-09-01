package com.myforum.tables.dao;

import java.util.Calendar;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myforum.application.CryptWithPBKDF2;
import com.myforum.application.DBHelper;
import com.myforum.application.StringLogics;
import com.myforum.security.CredentialLogics;
import com.myforum.tables.ForumUser;
import com.myforum.tables.Message;

public class ForumUserDao extends HibernateDao<ForumUser, Integer>{

	private static final long serialVersionUID = 1L;
   	private static Logger log = LoggerFactory.getLogger(ForumUserDao.class);

	public ForumUser find( Message message ){
   		ForumUser forumUser = message.getForumUser();
    	
        return forumUser; 
    }
	
	public ForumUser findByUsername( String username ){
		Session session = prepareTransaction();

    	Criteria criteria = session.createCriteria( daoType );
    	criteria.add(Restrictions.eq( "username", username ) );
    	
    	ForumUser forumUser = null;
    	if( criteria.list().size() > 0 ){
    		forumUser = (ForumUser) criteria.list().get( 0 );
    	}
    	
        return forumUser; 
    }

	public ForumUser findByEmailAddress( String emailAddress ){
		Session session = prepareTransaction();

    	Criteria criteria = session.createCriteria( daoType );
    	criteria.add(Restrictions.eq( "emailAddress", emailAddress ) );
    	
    	ForumUser forumUser = null;
    	if( criteria.list().size() > 0 ){
    		forumUser = (ForumUser) criteria.list().get( 0 );
    	}
    	
        return forumUser; 
    }

	public void addMessage( ForumUser forumUser, Message message ){
		Session session = prepareTransaction();
		session.refresh( message );
		session.refresh( forumUser );
		forumUser.getMessages().add( message );
	}


	public String generateNewPassword(ForumUser user){
		String newPassword 	= CredentialLogics.passwordGenerator( 10 );
		return setNewPassword(user, newPassword);
	}

	public String setNewPassword(ForumUser user, String newPassword){
		if(user == null){
			return "";
		}
		if(StringLogics.isEmpty(newPassword)){
			return "";
		}
		
        String encryptedPassword = "";
        try {
			encryptedPassword = CryptWithPBKDF2.generateStrongPasswordHash(newPassword);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Failed to generate a password hash.");
			return "";
		}
        
        user.setPassword(encryptedPassword);
		
		if(!update(user)){ 
			DBHelper.getTransaction().rollback();
			return "";
		}

		return newPassword;
	}
	
	public String generateMagicToken(ForumUser user){
		if(user == null){
			return "";
		}
		
        String magicToken = user.getUsername() + String.valueOf(Calendar.getInstance().getTimeInMillis());
        try {
        	magicToken = CryptWithPBKDF2.generateStrongPasswordHash(magicToken);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Failed to generate a magic token");
			return "";
		}
		return magicToken;
	}

}
