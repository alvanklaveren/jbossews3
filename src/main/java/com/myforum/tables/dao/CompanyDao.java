package com.myforum.tables.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.myforum.tables.Company;

public class CompanyDao extends HibernateDao<Company, Integer>{

	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Company> list(){
		Session session = prepareTransaction();

    	Criteria criteria = session.createCriteria( daoType );
    	criteria.addOrder( Order.asc( "description" ) );
    	
        return (List<Company>) criteria.list(); 
    }

	public Company findByDescription( String description ){
		Session session = prepareTransaction();

    	Criteria criteria = session.createCriteria( daoType );
    	criteria.add(Restrictions.eq( "description", description ).ignoreCase() );
    	
    	Company company = null;
    	if( criteria.list().size() > 0 ){
    		company = (Company) criteria.list().get( 0 );
    	}
    	
        return company; 
    }
	
}
