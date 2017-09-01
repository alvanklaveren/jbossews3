package com.myforum.tables.dao;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.myforum.tables.Message;
import com.myforum.tables.MessageCategory;


public class MessageCategoryDao extends HibernateDao<MessageCategory, Integer>{
	private static final long serialVersionUID = 1L;

	public int getMessageCount( MessageCategory messageCategory ){
    	Session session = prepareTransaction();
		session.refresh(messageCategory);	    		

		int messageCount=0;
		
		Set<Message> messageSet = messageCategory.getMessages();
		// count messages in category EXCLUDING replies to messages ( when column "code_message" <> 0 )
    	if( messageSet.size() > 0 ){
	    	Iterator<Message> itr = messageSet.iterator();
	    	while(itr.hasNext()){
	    		Message message = itr.next();
	    		if (message.getMessage() == null) messageCount += 1;
	    	}
    	}
    	    	
    	return messageCount;
	}

	@SuppressWarnings("unchecked")
	public List<MessageCategory> find( String description ){
		Session session = prepareTransaction();

    	Criteria criteria = session.createCriteria( daoType );
    	criteria.add(Restrictions.eq( "description", description ) );
    	
        return criteria.list(); 
    }

	public void addMessage( MessageCategory messageCategory, Message message ){
		Session session = prepareTransaction();
		session.refresh( message );
		session.refresh( messageCategory );
		messageCategory.getMessages().add( message );
	}
	
	public List<MessageCategory> removeCategory(List<MessageCategory> messageCategories, int category){
		for(int position = messageCategories.size() - 1; position >= 0; position--){
			if( messageCategories.get(position).getCode() == category ){
				messageCategories.remove(position);
			}
		}
		return messageCategories;
	}

}
