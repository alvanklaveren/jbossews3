package com.myforum.tables.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.myforum.tables.Message;
import com.myforum.tables.MessageImage;

public class MessageImageDao extends HibernateDao<MessageImage, Integer>{

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public List<MessageImage> list( Message message ){
		Session session = prepareTransaction();

    	Criteria criteria = session.createCriteria( daoType );
    	criteria.add(Restrictions.eqOrIsNull( "message.code", message.getCode() ) );
    	criteria.addOrder( Order.asc( "code" ) );
    	
        return (List<MessageImage>) criteria.list(); 
    }

	public List<MessageImage> getAllImages( Message message ) {

		return list(message);
	}

	public int getNumberOfImages( Message message ) {

		return list(message).size();
	}

	public MessageImage getImageByCode( int code ){
		
		return find(code);	
    }

	public MessageImage getImage( Message message, int index ){
		
		List<MessageImage> imageList = list(message);
		
        if( imageList == null || imageList.size() <= 0 || imageList.size() <= index ){
        	return null;
        };
        
        return imageList.get(index);
    }

}
