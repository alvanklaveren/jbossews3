package com.myforum.security;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myforum.base.AVKPage;
import com.myforum.tables.Classification;

public abstract class UserRole {
	private static Logger log = LoggerFactory.getLogger(UserRole.class);
	
	@SuppressWarnings("rawtypes")
	HashMap<Class, WindowPrivilege> windowPrivilegeMap = new HashMap<Class, WindowPrivilege>();
	
	protected Classification classification = new Classification();
	
	@SuppressWarnings("rawtypes")
	protected HashMap<Class, WindowPrivilege> getWindowPrivileges(){
		return windowPrivilegeMap;
	}

	@SuppressWarnings("rawtypes") 
	protected void addPrivilege(Class windowId, boolean canRead, boolean canWrite, boolean canOpen){
		windowPrivilegeMap.put( windowId, new WindowPrivilege(AVKPage.class, canRead, canWrite, canOpen) );
	}

	@SuppressWarnings("rawtypes")
	public WindowPrivilege getWindowPrivilege(Class windowId){
		WindowPrivilege windowPrivilege = windowPrivilegeMap.get(windowId);
		if(windowPrivilege==null){ 
			log.error("Assertion Failure: No privileges assigned for class " + windowId.toString());
		}
		
		return windowPrivilege;
	};

}
