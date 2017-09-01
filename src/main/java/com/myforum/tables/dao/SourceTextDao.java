package com.myforum.tables.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.myforum.tables.SourceSubject;
import com.myforum.tables.SourceText;
import com.myforum.tables.SourceType;

public class SourceTextDao extends HibernateDao<SourceText, Integer>{

	private static final long serialVersionUID = 1L;

	public SourceText find( int codeSourceSubject, int codeSourceType ){
		Session session = prepareTransaction();

    	Criteria criteria = session.createCriteria( daoType );
    	criteria.add(Restrictions.eqOrIsNull( "sourceSubject.code", codeSourceSubject ) );
    	criteria.add(Restrictions.eqOrIsNull( "sourceType.code", codeSourceType ) );
    	criteria.addOrder( Order.desc( "code" ) );
    	
    	@SuppressWarnings("unchecked")
		List<SourceText> sourceTextList = (List<SourceText>) criteria.list();
    	
        return sourceTextList.size() > 0 ? sourceTextList.get(0) : null; 
    }

	@SuppressWarnings("unchecked")
	public List<SourceText> list( SourceSubject sourceSubject ){
		Session session = prepareTransaction();

    	Criteria criteria = session.createCriteria( daoType );
    	criteria.add(Restrictions.eqOrIsNull( "sourceSubject.code", sourceSubject.getCode() ) );
    	criteria.addOrder( Order.desc( "code" ) );
    	
        return (List<SourceText>) criteria.list(); 
    }

	@SuppressWarnings("unchecked")
	public List<SourceText> list( SourceType sourceType ){
		Session session = prepareTransaction();

    	Criteria criteria = session.createCriteria( daoType );
    	criteria.add(Restrictions.eqOrIsNull( "sourceType.code", sourceType.getCode() ) );
    	criteria.addOrder( Order.desc( "code" ) );
    	
        return (List<SourceText>) criteria.list(); 
    }

}
