package com.myforum.tables.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.myforum.tables.Product;
import com.myforum.tables.ProductRating;

public class ProductRatingDao extends HibernateDao<ProductRating, Integer>{
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ProductRating> list(){
		Session session = prepareTransaction();

    	Criteria criteria = session.createCriteria( daoType );
    	criteria.addOrder( Order.asc( "code" ) );
    	
        return (List<ProductRating>) criteria.list(); 
    }

	@SuppressWarnings("unchecked")
	public int findByDescription( String description ){
		Session session = prepareTransaction();

    	Criteria criteria = session.createCriteria( daoType );
    	criteria.add(Restrictions.eq( "description", description ) );
    	
    	List<ProductRating> list = criteria.list();
    	int findKey = 0;
    	if( list.get(0) != null){
    		findKey = list.get(0).getCode();
    	}
    	
        return findKey; 
    }

	@SuppressWarnings("unchecked")
	public ProductRating findByProduct( int codeProduct ){
		Session session = prepareTransaction();
		ProductRating productRating = null; 
		
		
    	Criteria criteria = session.createCriteria( daoType );
    	criteria.add(Restrictions.eq( "product.code",  codeProduct ) );
    	
    	List<ProductRating> list = criteria.list();
    	if( list.get(0) != null){
    		productRating = list.get(0);
    	}
    	
        return productRating; 
    }

	@SuppressWarnings("unchecked")
	public ProductRating findOrCreateByProduct( int codeProduct ){
		Session session = prepareTransaction();

		// if it cannot be found, create it
		ProductRating productRating = new ProductRating(); 
		
		
    	Criteria criteria = session.createCriteria( daoType );
    	criteria.add(Restrictions.eq( "product.code", codeProduct ) );
    	
    	List<ProductRating> list = criteria.list();
    	if(list.size() > 0){
    		productRating = list.get(0);
    	}
    	Product product = new ProductDao().find(codeProduct);
    	productRating.setProduct(product);
    	
        return productRating; 
    }

	@SuppressWarnings("unchecked")
	public ProductRating findOrCreateByProduct( Product product ){
		Session session = prepareTransaction();
		// if it cannot be found, create it
		ProductRating productRating = new ProductRating(); 
				
    	Criteria criteria = session.createCriteria( daoType );
    	criteria.add(Restrictions.eq( "product.code", product.getCode() ) );
    	
    	List<ProductRating> list = criteria.list();
    	if( list.get(0) != null){
    		productRating = list.get(0);
    	}

    	productRating.setProduct(product);
    	
        return productRating; 
    }

}
