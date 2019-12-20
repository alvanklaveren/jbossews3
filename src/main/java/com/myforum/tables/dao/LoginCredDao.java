package com.myforum.tables.dao;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myforum.application.DBHelper;
import com.myforum.application.ForumUtils;
import com.myforum.tables.ForumUser;
import com.myforum.tables.LoginCred;

public class LoginCredDao extends HibernateDao<LoginCred, Integer>{
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(LoginCredDao.class);
	
	@SuppressWarnings("unchecked")
	public List<LoginCred> find(ForumUser forumUser){
		Session session = prepareTransaction();

    	Criteria criteria = session.createCriteria( daoType );
    	criteria.add(Restrictions.eq( "forumUser.code", forumUser.getCode() ) );
    	
        return criteria.list(); 
    }

	@SuppressWarnings("unchecked")
	public List<LoginCred> findByMagicToken(String magicToken){
		Session session = prepareTransaction();

    	Criteria criteria = session.createCriteria( daoType );
    	criteria.add(Restrictions.eq( "magicToken", magicToken ) );
    	
        return criteria.list(); 
    }

	@SuppressWarnings("unchecked")
	public List<LoginCred> findByForumUser(ForumUser user){
		Session session = prepareTransaction();

    	Criteria criteria = session.createCriteria( daoType );
    	criteria.add(Restrictions.eq( "forumUser.code", user.getCode() ) );
    	
        return criteria.list(); 
    }

	public boolean tokenExpired(String token){
		if( token == null || token.length() <= 0 ){ return true; }
		Session session = prepareTransaction();
    	Criteria criteria = session.createCriteria( daoType );
    	criteria.add(Restrictions.eq( "magicToken", token ) );
    	
    	@SuppressWarnings("unchecked")
		List<LoginCred> loginCreds = criteria.list();
    	
    	// if no token found, token is by default expired
    	if( loginCreds.size() <= 0 ){
    		return true;
    	}
    	
    	// empty all tokens, if somewhere by accident the same token was provided
    	if( loginCreds.size() > 1 ){
    		for(LoginCred loginCred:loginCreds){
    			loginCred.setMagicToken(""); 
        		DBHelper.saveAndCommit(loginCred);
    		}
    		return true;
    	}

    	LoginCred loginCred = loginCreds.get(0);
    	
    	// make sure the token is no longer valid after 30 minutes
    	Date tokenDate = loginCred.getTokenDate();
    	long diff = ForumUtils.todayNow().getTime() - tokenDate.getTime();
    	if( TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS) >= 30){
    		loginCred.setMagicToken(""); // if expired, then also remove token from database
    		DBHelper.saveAndCommit(loginCred);
    		return true;
    	}
    	
    	// if token is not expired, then reset the expiration timer by applying a new date and time
    	// but do not do this too often, as this function is called many times at once
    	if( TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS) >= 5){
   	    	loginCred.setTokenDate(new java.sql.Date( ForumUtils.todayNow().getTime()));

   	    	if(DBHelper.saveAndCommit(loginCred) == null){
   	    		log.error("Failed to update LoginCred record.");
   	    		return false;	
   	    	};
    	}

		return false;
	}
	
	public ForumUser getForumUserByMagicToken(String magicToken){
		if( tokenExpired(magicToken)){
			return null;
		}
		
		List<LoginCred> list = findByMagicToken(magicToken);
		if(list.size() == 0){ 
			return null; 
		}
		return list.get(0).getForumUser();
	}

	public LoginCred getLoginCredByMacAddress(String macAddress){
		Session session = prepareTransaction();

    	Criteria criteria = session.createCriteria( daoType );
    	criteria.add(Restrictions.eq( "logincred.macAddress", macAddress ) );
    	
    	if(criteria.list().isEmpty()){
    		return null;   		
    	}
        return (LoginCred) criteria.list(); 
	}
	
	public boolean setMagicToken(ForumUser user, String magicToken){
		List<LoginCred> list = findByForumUser(user);
		LoginCred loginCred  = null;
		
		if(list.size()!=0){
			loginCred = list.get(0);
		}

		if(loginCred==null){
			loginCred = new LoginCred();
			loginCred.setMacAddress(user.getUsername()); // TODO: For the time being, we will ignore mac-address (only used in mobile application)
			loginCred.setForumUser(user);
		}
		
		loginCred.setMagicToken(magicToken);
		loginCred.setTokenDate(new java.sql.Date( ForumUtils.todayNow().getTime()));
		
		if(DBHelper.saveAndCommit(loginCred) == null){
			log.error("Failed to update LoginCred record.");
			return false;	
		};
		
		return true;
	}
}
