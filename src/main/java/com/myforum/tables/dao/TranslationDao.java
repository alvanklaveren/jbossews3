package com.myforum.tables.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.myforum.tables.Translation;

public class TranslationDao extends HibernateDao<Translation, Integer>{

	private static final long serialVersionUID = 1L;
	
	public String findByLanguage( String language, String original ){
				
		Session session = prepareTransaction();

    	Criteria criteria = session.createCriteria( daoType );
    	criteria.add(Restrictions.eq( "original", original ));
    	
    	if( criteria.list().size() == 0 ){
    		return null;   			
    	}

    	Translation translation = (Translation) criteria.list().get(0);
    	
		switch(language.toLowerCase()) {
			case "us": 
				return translation.getUs(); 
	
			case "nl": 
				return translation.getNl(); 
	
			default:
				return translation.getOriginal(); 
		}
	}
   	
}
