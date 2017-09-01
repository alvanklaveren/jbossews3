package com.myforum.tables.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.myforum.tables.SourceSubject;
import com.myforum.tables.SourceText;
import com.myforum.tables.SourceType;


public class SourceTypeDao extends HibernateDao<SourceType, Integer>{
	private static final long serialVersionUID = 1L;

	public SourceType find( SourceText sourceText ){   	
        return sourceText.getSourceType(); 
    }

	public SourceType find( SourceSubject sourceSubject ){
		Session session = prepareTransaction();

		SourceType sourceType = (SourceType) session.load( SourceType.class, sourceSubject.getCode() );
    	
        return sourceType; 
    }

	@SuppressWarnings("unchecked")
	public List<SourceType> find( String description ){
		Session session = prepareTransaction();

    	Criteria criteria = session.createCriteria( daoType );
    	criteria.add(Restrictions.eq( "description", description ) );
    	
        return ( List<SourceType> ) criteria.list(); 
    }

	public void addSourceText( SourceType sourceType, SourceText sourceText ){
		Session session = prepareTransaction();
		session.refresh( sourceText );
		session.refresh( sourceType );
		sourceType.getSourceTexts().add( sourceText );
	}
}
