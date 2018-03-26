package com.myforum.tables.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.myforum.tables.ProductType;

public class ProductTypeDao extends HibernateDao<ProductType, Integer>{

	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ProductType> list(){
		Session session = prepareTransaction();

    	Criteria criteria = session.createCriteria( daoType );
    	criteria.addOrder( Order.asc( "description" ) );
    	
        return (List<ProductType>) criteria.list(); 
    }


	@SuppressWarnings("unchecked")
	public int findKey( String description ){
		Session session = prepareTransaction();

    	Criteria criteria = session.createCriteria( daoType );
    	criteria.add(Restrictions.eq( "description", description ) );
    	
    	List<ProductType> list = criteria.list();
    	int findKey = 0;
    	if( list.get(0) != null){
    		findKey = list.get(0).getCode();
    	}
    	
        return findKey; 
    }
	
	public ProductType findByDescription( String description ){
		Session session = prepareTransaction();

    	Criteria criteria = session.createCriteria( daoType );
    	criteria.add(Restrictions.eq( "description", description ).ignoreCase() );
    	
    	ProductType productType = null;
    	if( criteria.list().size() > 0 ){
    		productType = (ProductType) criteria.list().get( 0 );
    	}
    	
        return productType; 
    }
}
