package com.myforum.tables.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.myforum.application.StringLogics;
import com.myforum.gameshop.ESortOrder;
import com.myforum.gameshop.GameShopLogics;
import com.myforum.tables.GameConsole;
import com.myforum.tables.Product;
import com.myforum.tables.ProductType;

public class ProductDao extends HibernateDao<Product, Integer>{

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> list(){
		Session session = prepareTransaction();
    	
		Criteria criteria = session.createCriteria( daoType );
    	criteria.createAlias( "productType", "pt" );
    	criteria.addOrder( Order.asc( "pt.description" ) );
    	criteria.addOrder( Order.asc( "name" ) );
    	
        return (List<Product>) criteria.list();
    }

	@SuppressWarnings("unchecked")
	public List<Product> listUniqueNames(){
		Session session = prepareTransaction();
    	
		Criteria criteria = session.createCriteria( daoType ).setProjection(
	    Projections.distinct(Projections.projectionList()
	    	       .add(Projections.property("name"), "name")))
	    		   .setResultTransformer(Transformers.aliasToBean(Product.class));
		
    	criteria.addOrder( Order.asc( "name" ) );
    	//criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // not unique enough
    	
        return (List<Product>) criteria.list();
    }

	/*
	 * Returns a List of products containing the most recently added
	 */
	@SuppressWarnings("unchecked")
	public List<Product> list(int numberOfResults){
		Session session = prepareTransaction();

		Criteria criteria = session.createCriteria( daoType );
    	criteria.addOrder( Order.desc( "code" ) );

		if( numberOfResults >= 1 ){
			criteria.setMaxResults(numberOfResults);
		}
    	
		List<Product> productList = criteria.list();
    	
        return productList;
    }

	@SuppressWarnings("unchecked")
	public List<Product> list( int gameConsoleCode, int productTypeCode, ESortOrder sortOrder ){
		Session session = prepareTransaction();

    	Criteria criteria = session.createCriteria( daoType );
    	
		// prevent duplicate results because of using restrictions.disjunction and LIKE operators
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
    	
    	if( gameConsoleCode != 0 ){
    		criteria.add(Restrictions.eqOrIsNull( "gameConsole.code", gameConsoleCode ) );
    	}
    	if( productTypeCode != 0 ){
    		criteria.add(Restrictions.eqOrIsNull( "productType.code", productTypeCode ) );
    	}
    	criteria.createAlias( "productType", "pt" );
    	criteria.addOrder( Order.asc( "pt.description" ) );
    	
    	criteria.createAlias( "gameConsole", "gc" );
    	criteria.addOrder( Order.asc( "gc.description" ) );
 	
    	switch(sortOrder) {
    	case AZ:    	criteria.addOrder( Order.asc(  "name" ) );	break;
    	case ZA:		criteria.addOrder( Order.desc( "name" ) );	break;
    	case Rating: 	
    					// Careful: using this alias eliminates all results from the list that do NOT have a rating 
    					criteria.createAlias( "productRatings", "prs" );
    					criteria.addOrder( Order.asc(  "prs.rating" ) ); 	
    					criteria.addOrder( Order.asc(  "name" ) );
    					criteria.add(Restrictions.neOrIsNotNull( "prs.rating", 0 ) );
    					break; 
    					
		default:		criteria.addOrder( Order.asc(  "name" ) );	break;
    	}
    	
    	

        return (List<Product>) criteria.list(); 
    }

	public List<Product> list( GameConsole gameConsole, ESortOrder sortOrder ){
		int gameConsoleCode = 0;
		int productTypeCode = 0;
		if( gameConsole != null){	gameConsoleCode = gameConsole.getCode(); }
		return list(gameConsoleCode, productTypeCode, sortOrder);
    }

	public List<Product> list( ProductType productType, ESortOrder sortOrder ){
		int gameConsoleCode = 0;
		int productTypeCode = 0;
		if( productType != null){	productTypeCode = productType.getCode(); }
		return list(gameConsoleCode, productTypeCode, sortOrder);
    }

	public List<Product> list( GameConsole gameConsole, ProductType productType, ESortOrder sortOrder ){
		int gameConsoleCode = 0;
		int productTypeCode = 0;
		
		if( gameConsole != null ){	gameConsoleCode = gameConsole.getCode(); }
		if( productType != null ){	productTypeCode = productType.getCode(); } 
		return list(gameConsoleCode, productTypeCode, sortOrder);

    }

	@SuppressWarnings("unchecked")
	public List<Product> search( String title ){
		Session session = prepareTransaction();

		Criteria criteria = session.createCriteria( daoType );
		
		// prevent duplicate results because of using restrictions.disjunction and LIKE operators
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
    	
    	// translate (roman) numbers, if any. E.g. a 2 becomes II, and a IV becomes 4
		String convertedTitle = StringLogics.convertVersionNumbers(title);
		
		// to include even more hits, replace the spaces with wildcards, just in case the whitespace is missing in a word,
		// e.g. when you search for far cry, and somewhere accidently the title description said farcry	
		title = title.replace(" ", "%");
		convertedTitle = convertedTitle.replace(" ", "%");
		
		// next surround with wildcards, so "fin" will also fetch "final" and "race" also fetches "trace"
		title = "%" + title + "%";
		convertedTitle = "%" + convertedTitle + "%";
 	
    	// using disjunction creates an OR expression for the below LIKE's.
    	criteria.add(Restrictions.disjunction()
					 .add(Restrictions.like("name", title, MatchMode.ANYWHERE)) // matches anywhere between and including begin and end of name
					 .add(Restrictions.like("name", convertedTitle, MatchMode.ANYWHERE)) // again matches anywhere, but this time has (roman) numbers converted
	        		);
	
    	criteria.addOrder( Order.asc( "name" ) );
    	
    	List<Product> defaultList = (List<Product>) criteria.list();
    	
    	defaultList = GameShopLogics.sortProductList(defaultList, ESortOrder.AZ);
    	
    	return defaultList;
    }

}
