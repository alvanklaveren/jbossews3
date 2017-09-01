package com.myforum.tables.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.myforum.tables.ForumUser;
import com.myforum.tables.Notifications;

public class NotificationsDao extends HibernateDao<Notifications, Integer>{
	private static final long serialVersionUID = 1L;

	public List<Notifications> find( ForumUser forumUser ){
		return find( forumUser, false );
	}

	@SuppressWarnings("unchecked")
	public List<Notifications> find( ForumUser forumUser, boolean onlyMustSend ){
		Session session = prepareTransaction();

    	Criteria criteria = session.createCriteria( daoType );
    	criteria.add(Restrictions.eq( "codeForumUser", forumUser.getCode() ) );
    	if( onlyMustSend ){ 
    		criteria.add(Restrictions.eq( "mustSend", "y"));
    	}
    	
        return criteria.list(); 
    }

}
