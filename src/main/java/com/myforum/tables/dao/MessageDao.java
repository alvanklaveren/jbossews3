package com.myforum.tables.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.myforum.tables.Message;
import com.myforum.tables.MessageCategory;

public class MessageDao extends HibernateDao<Message, Integer>{

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public List<Message> getMessages( MessageCategory messageCategory ){
		List<Message> messageList = new ArrayList<>();
		
		Session session = prepareTransaction();

    	Criteria criteria = session.createCriteria( daoType );
    	criteria.add(Restrictions.eqOrIsNull( "messageCategory.code", messageCategory.getCode() ) );
    	criteria.add(Restrictions.isNull("message"));
    	criteria.addOrder( Order.desc( "messageDate" ) );
    	
    	try {
    		messageList = criteria.list();
    	}catch(Exception e) {
    		System.out.println("My Error: " + e.getMessage());
    		e.printStackTrace();
    		throw new RuntimeException("FAIL!!");
    	}
        return messageList;
    }
	
	@SuppressWarnings("unchecked")
	public List<Message> getThreadMessages( Message message ){
		Session session = prepareTransaction();

    	Criteria criteria = session.createCriteria( daoType );
    	criteria.add(Restrictions.eqOrIsNull( "message.code", message.getCode() ) );
    	criteria.addOrder( Order.asc( "messageDate" ) );
    	
        return criteria.list(); 
    }

}
