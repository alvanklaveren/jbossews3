package com.myforum.tables.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.myforum.tables.SourceCategory;
import com.myforum.tables.SourceSubject;

public class SourceSubjectDao extends HibernateDao<SourceSubject, Integer>{

	private static final long serialVersionUID = 1L;

	@SuppressWarnings( "unchecked" )
	public List<SourceSubject> list( SourceCategory sourceCategory ){
		Session session = prepareTransaction();

    	Criteria criteria = session.createCriteria( daoType );
    	criteria.add(Restrictions.eqOrIsNull( "sourceCategory.code", sourceCategory.getCode() ) );
    	criteria.addOrder( Order.desc( "code" ) );
    	
        return criteria.list(); 
    }	

}
