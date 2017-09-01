package com.myforum.application;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CryptWithMD5 {
	   private static MessageDigest md;
	   private static CryptWithMD5 instance;
	   private static Logger log = LoggerFactory.getLogger(CryptWithMD5.class);
	   
	   private CryptWithMD5(){};
	   
	   public CryptWithMD5 getInstance(){
		   if(instance == null){
			   instance = new CryptWithMD5(); 
		   }
		   return instance;
	   }

	   public static String cryptWithMD5(String password, long passwordSalt){
		   try {
		        md = MessageDigest.getInstance("SHA-256");
		        byte[] passwordBytes = ( password + String.valueOf(passwordSalt) ).getBytes();
		        md.reset();
		        byte[] digested = md.digest(passwordBytes);
		        StringBuffer sb = new StringBuffer();
		        for(int i=0;i<digested.length;i++){
		            sb.append(Integer.toHexString(0xff & digested[i]));
		        }
		        return sb.toString();
		    } catch (NoSuchAlgorithmException ex) {
		    	log.error("Failed to encrypt password");
		    }
		    return null;
	   }
	}