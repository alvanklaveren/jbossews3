package com.myforum.tables.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.myforum.tables.Constants;

public class ConstantsDao extends HibernateDao<Constants, Integer>{

	private static final long serialVersionUID = 1L;
	
	public Constants findById( String id ){
		Session session = prepareTransaction();

    	Criteria criteria = session.createCriteria( daoType );
    	criteria.add(Restrictions.eq( "id", id ).ignoreCase() );
    	
    	Constants constants = null;
    	if( criteria.list().size() > 0 ){
    		constants = (Constants) criteria.list().get( 0 );
    	}
    	
        return constants; 
    }


}
