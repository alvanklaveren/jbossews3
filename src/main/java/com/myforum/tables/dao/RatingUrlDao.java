package com.myforum.tables.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.myforum.tables.RatingUrl;

public class RatingUrlDao extends HibernateDao<RatingUrl, Integer>{

	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RatingUrl> list(){
		Session session = prepareTransaction();

    	Criteria criteria = session.createCriteria( daoType );
    	criteria.addOrder( Order.asc( "url" ) );
    	
        return (List<RatingUrl>) criteria.list(); 
    }
	
	@SuppressWarnings("unchecked")
	public int findKey( String url ){
		Session session = prepareTransaction();

    	Criteria criteria = session.createCriteria( daoType );
    	criteria.add(Restrictions.eq( "url", url ) );
    	
    	List<RatingUrl> list = criteria.list();
    	int findKey = 0;
    	if( list.size() > 0){
    		findKey = list.get(0).getCode();
    	}
    	
        return findKey; 
    }
}
