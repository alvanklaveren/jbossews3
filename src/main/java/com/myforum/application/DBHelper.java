package com.myforum.application;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myforum.tables.Classification;
import com.myforum.tables.Company;
import com.myforum.tables.Constants;
import com.myforum.tables.ForumUser;
import com.myforum.tables.GameConsole;
import com.myforum.tables.LoginCred;
import com.myforum.tables.Message;
import com.myforum.tables.MessageCategory;
import com.myforum.tables.MessageImage;
import com.myforum.tables.Notifications;
import com.myforum.tables.Product;
import com.myforum.tables.ProductImage;
import com.myforum.tables.ProductRating;
import com.myforum.tables.ProductType;
import com.myforum.tables.RatingUrl;
import com.myforum.tables.SourceCategory;
import com.myforum.tables.SourceSubject;
import com.myforum.tables.SourceText;
import com.myforum.tables.SourceType;


public final class DBHelper {

	private static ServiceRegistry serviceRegistry;
   	private static SessionFactory sessionFactory;
   	// private static final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();
   	private static DBHelper instance;
   	private static Logger log = LoggerFactory.getLogger(DBHelper.class);
    
    private DBHelper(){
        try {
            sessionFactory = configureSessionFactory();
          } catch (Exception e) {
        	  log.error("%%%% Error Creating SessionFactory %%%% ");
        	  e.printStackTrace();
          }
    }

    public static DBHelper getInstance(){
    	if(instance == null ){
    		instance = new DBHelper();
    	}
    	return instance;
    }
    
	private static SessionFactory configureSessionFactory() {
	    try {
	        Configuration configuration = new Configuration();
	        configuration.configure("hibernate.cfg.xml");

	        configuration.addAnnotatedClass(Constants.class);
	        configuration.addAnnotatedClass(MessageCategory.class);
	        configuration.addAnnotatedClass(Message.class);
	        configuration.addAnnotatedClass(MessageImage.class);
	        configuration.addAnnotatedClass(Classification.class);
	        configuration.addAnnotatedClass(LoginCred.class);
	        configuration.addAnnotatedClass(Notifications.class);
	        configuration.addAnnotatedClass(SourceCategory.class);
	        configuration.addAnnotatedClass(SourceSubject.class);
	        configuration.addAnnotatedClass(SourceType.class);
	        configuration.addAnnotatedClass(SourceText.class);
	        configuration.addAnnotatedClass(ForumUser.class);

	        configuration.addAnnotatedClass(Company.class);
	        configuration.addAnnotatedClass(GameConsole.class);
	        configuration.addAnnotatedClass(ProductType.class);
	        configuration.addAnnotatedClass(Product.class);
	        configuration.addAnnotatedClass(ProductImage.class);
	        configuration.addAnnotatedClass(ProductRating.class);
	        configuration.addAnnotatedClass(RatingUrl.class);

	        configuration.configure();
	        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
	        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
	
	        return sessionFactory;
	    } catch (HibernateException e) {
	    	log.error("** Exception in SessionFactory **");
	            e.printStackTrace();
	        }
	       return sessionFactory;
	  }


  public static void rebuildSessionFactory() {
	    try {
	      sessionFactory = configureSessionFactory();
	    } catch (Exception e) {
	       	log.error("%%%% Error Creating SessionFactory %%%%");
	       	e.printStackTrace();
	    }
}

  public static SessionFactory getSessionFactory() {
	  if( sessionFactory == null || sessionFactory.isClosed() ){
		  rebuildSessionFactory();
	  }
	  
    return sessionFactory;
  }

  public static Session openSession() {
	  Session session = null;
	   
  	  try{		  
  		  if (sessionFactory == null) { rebuildSessionFactory(); }
  		  
  		  session = getSessionFactory().getCurrentSession();
	  		
  		  if (session == null || !session.isOpen()) {
  			  session = getSessionFactory().openSession();
  		  }
  		
  		  // but maybe both sessionfactory and session are open already, so check if there is a valid transaction
  		  if( session != null && session.isOpen() && session.getTransaction() != null && sessionFactory.getCurrentSession().getTransaction().getStatus() == TransactionStatus.ACTIVE){
  			  sessionFactory.getCurrentSession().getTransaction().commit();
  			  session = getSessionFactory().openSession();
  		  }

  		  
  	  }catch(HibernateException e){
  		  rebuildSessionFactory();
  		  session = getSessionFactory().openSession();
  		  e.printStackTrace();
  	  }

  	  if( session != null) { session.beginTransaction(); }
  	  
  	  return session;
  	  
  }
  
  public static void closeSession(){
	  	if(getSessionFactory() == null) return;
	  	if(getSessionFactory().getCurrentSession() == null) return;
	  	if(getSessionFactory().getCurrentSession().isOpen()) { 
	  		getSessionFactory().getCurrentSession().close(); 
	  	}
  }

  public static Transaction getTransaction(){
	  if (sessionFactory == null) { rebuildSessionFactory(); }
	  
	  Session session = getSessionFactory().getCurrentSession();
	
	  if (session == null || !session.isOpen()) {
		  session = sessionFactory.openSession();
		  session.beginTransaction();
	  }
	  
	  return session.getTransaction();	 
  }
  
  public static Object saveAndCommit( Object object ){
	Session session = openSession();
	Object newRef = session.merge(object);
	Transaction transaction = session.getTransaction();
	session.saveOrUpdate( newRef );	        	
	try{
		transaction.commit();
	}catch( Exception ex){
		ex.printStackTrace();
		transaction.rollback();
		return null;
	}
	
	return newRef;
  }

  public static boolean deleteAndCommit( Object object ){
	Session session = openSession();
	Object newRef = session.merge(object);
    Transaction transaction = session.getTransaction();
    session.delete(newRef);	
	try{
		transaction.commit();
	}catch( Exception ex){
		ex.printStackTrace();
		transaction.rollback();
		return false;
	}
	return true;
  }

  public static boolean rollback(){
	Session session = openSession();
	if( !session.isOpen() ) return true;
	Transaction transaction = session.getTransaction();
	try{
		transaction.rollback();
		if( !session.isOpen() ) return true;
		session.close();
	}catch( Exception ex){
		ex.printStackTrace();
		return false;
	}
	
	return true;
  }

  
}