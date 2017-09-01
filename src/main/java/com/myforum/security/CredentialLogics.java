package com.myforum.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myforum.application.CookieLogics;
import com.myforum.application.CryptWithPBKDF2;
import com.myforum.tables.ForumUser;
import com.myforum.tables.dao.ForumUserDao;

public final class CredentialLogics{
	
	static{ new CredentialLogics(); }

   	private static Logger log = LoggerFactory.getLogger(CredentialLogics.class);

	private CredentialLogics(){}

	public static boolean isAdministrator(ForumUser forumUser){
		if( forumUser == null || forumUser.getClassification() == null || Integer.valueOf(forumUser.getClassification().getIsAdmin()) == null ){
			forumUser = null;
			CookieLogics.deleteCookie("magictoken");
			return false; 
		}
		if(forumUser.getClassification().getIsAdmin() != 1){ return false; }
		return true;
	}

	public static boolean isMember(ForumUser forumUser){
		if( forumUser == null){ return false; }
		if(forumUser.getClassification().getIsAdmin()!=2){ return false;}
		return true;
	}

	public static String passwordGenerator( int length ){
		String passwordChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567";
		StringBuilder sb = new StringBuilder();
		for (int i= 0; i < length; i++) {
		    int k = (int) ( Math.random()*( passwordChars.length() -1 ) );
		    sb.append( passwordChars.charAt(k) );
		}
		String password = sb.toString();
		return password;
	}
	
	public static boolean validCredentials(String username, String password){
		ForumUser user = new ForumUserDao().findByUsername( username );
		if(user == null) return false;
			
		boolean isPasswordValid = false;	
		try {
			isPasswordValid = CryptWithPBKDF2.validatePassword(password, user.getPassword());
		} catch (Exception e) {
			e.printStackTrace();
			log.error( "CryptWithPBKDF2.validatePassword() failed");
		}
		
    	return isPasswordValid;
	}

	/*
	 * slowEquals is used to slow down the password check to make
	 * password hacking more difficult (slower). It should not lead to
	 * noticable performance issues when a user tries to log in.
	 */
	public static boolean slowEquals(byte[] a, byte[] b)
	{
		int diff = a.length ^ b.length;
		for(int i = 0; i < a.length && i < b.length; i++)
		diff |= a[i] ^ b[i];
		return diff == 0;
	}
	
	public static boolean canRead(ForumUser user, Object windowId){
		WindowPrivilege windowPrivilege = WindowPrivilegeFactory.getWindowPrivilege(user, windowId.getClass());
		return windowPrivilege.CanRead();		
	}

	public static boolean canWrite(ForumUser user, Object windowId){
		WindowPrivilege windowPrivilege = WindowPrivilegeFactory.getWindowPrivilege(user, windowId.getClass());
		return windowPrivilege.canWrite();		
	}

	public static boolean canOpen(ForumUser user, Object windowId){
		WindowPrivilege windowPrivilege = WindowPrivilegeFactory.getWindowPrivilege(user, windowId.getClass());
		return windowPrivilege.canOpen();		
	}

}
