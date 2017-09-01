package com.myforum.tables.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myforum.application.DBHelper;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
	 
	/**
	 * Basic DAO operations dependent with Hibernate's specific classes
	 */
	@SuppressWarnings("unchecked")
	public class HibernateDao<E, K extends Serializable>  implements Serializable, HibernateDaoInterface<E, K>{ 
		private static final long serialVersionUID = 1L;
	   	private static Logger log = LoggerFactory.getLogger(HibernateDao.class);
		
		protected Class<? extends E> daoType;

		public HibernateDao() {
	        daoType = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	    }
		
		protected Session prepareTransaction(){
			Session session = DBHelper.getSessionFactory().getCurrentSession();
	    	if( !session.isOpen() ){
	    		session = DBHelper.getSessionFactory().openSession();
	    	}

	    	if( session.getTransaction().getStatus() != TransactionStatus.ACTIVE ){
	    		session.beginTransaction();
	    	}
	    	
	    	return session;
		}
		
	    @Override
		public boolean add(E entity) {
	    	Session session = prepareTransaction();
	    	try{
	    		session.save(entity);
	    	} catch( HibernateException e){
	    		e.printStackTrace();
	    		return false;
	    	}
    		
	    	return true;
	    	
	    }

	    @Override
		public boolean update(E entity) {
	    	Session session = prepareTransaction();
	    	try{
	    		session.saveOrUpdate(entity);
	    	} catch( HibernateException e){
	    		e.printStackTrace();
	    		log.error("update of record failed");
	    		return false;
	    	}
	    	
	    	return true;	    		    	
	    }

	    @Override
		public boolean remove(E entity) {
	    	Session session = prepareTransaction();
	    	try{
	    		session.delete(entity);
	    	} catch( HibernateException e){
	    		e.printStackTrace();
	    		return false;
	    	}
	    	
	    	return true;
	    	
	    }

	    @Override
		public E find(K key) {
	    	Session session = DBHelper.getSessionFactory().getCurrentSession();
	    	if( !session.isOpen() ){
	    		session = DBHelper.getSessionFactory().openSession();
	    	}

	    	if( session.getTransaction().getStatus() != TransactionStatus.ACTIVE ){
	    		session.beginTransaction();
	    	}

	    	return (E) session.get(daoType, key);
	    }
	 
	    @Override
		public List<E> list() {
	    	Session session = DBHelper.getSessionFactory().getCurrentSession();
	    	if( !session.isOpen() ){
	    		session = DBHelper.getSessionFactory().openSession();
	    	}

	    	if( session.getTransaction().getStatus() != TransactionStatus.ACTIVE ){
	    		session.beginTransaction();
	    	}

	        return session.createCriteria(daoType).list();
	    }
	}