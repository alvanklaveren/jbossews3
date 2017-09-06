package com.myforum.application;
import org.apache.wicket.Application;
import org.apache.wicket.util.crypt.ICrypt;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.myforum.tables.Constants;

public final class AllConstants {
	
	public static final String FORUM_NAME = "AVK Forum";
	public static final String MY_EMAIL = getConstantStringValue("email_address");
	public static final String MY_EMAIL_PASSWORD = getConstantStringValue("email_password");
	
	public static final int ADMINISTRATOR	= 0;
	public static final int MEMBER 			= 1;
	public static final int GUEST 			= 2;
	
	public static final String COOKIE_DOMAIN = "www.alvanklaveren.com"; 
	public static final String COOKIE_PATH 	 = "/"; 
	
	public static ICrypt iCrypt;
	
	static{ 
			new AllConstants(); 
	}
	
	private AllConstants(){}

	public static ICrypt getCrypt(){
		if(iCrypt == null){
			iCrypt		= Application.get().getSecuritySettings().getCryptFactory().newCrypt();
		}
		return iCrypt;
	}
		
	public static String getConstantStringValue( String id ){
		Session 	session 	= DBHelper.openSession();
		Criteria 	criteria 	= session.createCriteria(Constants.class);
		String 		stringValue = null;
		criteria.add(Restrictions.eq("id", id));
		
		if( criteria.list().size() > 0 ){
			Constants myConstant = (Constants) criteria.list().get(0);
			stringValue = myConstant.getStringValue();
		}
		
		session.close();	
		return stringValue;
	}
	
	public static byte[] getConstantBlobValue( String id ){
		Session 	session 	= DBHelper.openSession();
		Criteria 	criteria 	= session.createCriteria(Constants.class);
		byte[] 		blobValue	= null;

		criteria.add(Restrictions.eq("id", id));		
		if( criteria.list().size() > 0 ){
			Constants myConstant = (Constants) criteria.list().get(0);
			blobValue = myConstant.getBlobValue();
		}

		session.close();
		return blobValue;
	}
	
}
