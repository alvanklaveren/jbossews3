package com.myforum.tables.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.myforum.tables.SourceCategory;
import com.myforum.tables.SourceSubject;


public class SourceCategoryDao extends HibernateDao<SourceCategory, Integer>{
	private static final long serialVersionUID = 1L;

	public SourceCategory find( SourceSubject sourceSubject ){
   	
        return sourceSubject.getSourceCategory(); 
    }

	@SuppressWarnings( "unchecked" )
	public List<SourceCategory> find( String description ){
		Session session = prepareTransaction();

    	Criteria criteria = session.createCriteria( daoType );
    	criteria.add(Restrictions.eq( "description", description ) );
    	
        return criteria.list(); 
    }

	public void addSourceSubject( SourceCategory sourceCategory, SourceSubject sourceSubject ){
		Session session = prepareTransaction();
		session.refresh( sourceSubject );
		session.refresh( sourceCategory );
		sourceCategory.getSourceSubjects().add( sourceSubject );
	}
}
