package com.myforum.tables.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.myforum.tables.Company;
import com.myforum.tables.GameConsole;

public class GameConsoleDao extends HibernateDao<GameConsole, Integer>{

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	public List<GameConsole> list(){
		Session session = prepareTransaction();

    	Criteria criteria = session.createCriteria( daoType );
    	criteria.addOrder( Order.asc( "description" ) );
    	
        return (List<GameConsole>) criteria.list(); 
    }

	@SuppressWarnings("unchecked")
	public List<GameConsole> list( Company company ){
		Session session = prepareTransaction();

    	Criteria criteria = session.createCriteria( daoType );
    	criteria.add(Restrictions.eqOrIsNull( "company.code", company.getCode() ) );
    	criteria.addOrder( Order.asc( "sortorder" ) );
    	criteria.addOrder( Order.asc( "description" ) );
    	
        return (List<GameConsole>) criteria.list(); 
    }

	@SuppressWarnings("unchecked")
	public int findKey( String description ){
		Session session = prepareTransaction();

    	Criteria criteria = session.createCriteria( daoType );
    	criteria.add(Restrictions.eq( "description", description ) );
    	
    	List<GameConsole> list = criteria.list();
    	int findKey = 0;
    	if( list.get(0) != null){
    		findKey = list.get(0).getCode();
    	}
    	
        return findKey; 
    }
	
	public GameConsole findByDescription( String description ){
		Session session = prepareTransaction();

    	Criteria criteria = session.createCriteria( daoType );
    	criteria.add(Restrictions.eq( "description", description ).ignoreCase() );
    	
    	GameConsole gameConsole = null;
    	if( criteria.list().size() > 0 ){
    		gameConsole = (GameConsole) criteria.list().get( 0 );
    	}
    	
        return gameConsole; 
    }
}
