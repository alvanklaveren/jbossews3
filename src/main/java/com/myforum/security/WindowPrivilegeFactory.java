package com.myforum.security;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myforum.tables.ForumUser;

/*
 * Factory to generate windowprivileges for a particular window based on the user classification.
 * This class is package private, i.e. it can and should not be referred to in other packages
 */
final class WindowPrivilegeFactory {
	private static HashMap<Integer, UserRole> userRoleMap = new HashMap<Integer, UserRole>();
	
	static{
			new WindowPrivilegeFactory();
			userRoleMap.put(0, new Guest());
			userRoleMap.put(1, new Administrator());
			userRoleMap.put(2, new Member());
	}

   	private static Logger log = LoggerFactory.getLogger(WindowPrivilegeFactory.class);

	private WindowPrivilegeFactory(){}

	/*
	 * Return the windowprivilege based on the user classification and the window ID
	 */
	@SuppressWarnings("rawtypes")
	public static WindowPrivilege getWindowPrivilege(ForumUser user, Class windowId){
		if (user == null){
			return getWindowPrivilege(0, windowId);			// returns as Guest
		}
		return getWindowPrivilege(user.getClassification().getIsAdmin(), windowId);
	}

	/*
	 * Return the windowprivilege based on the user classification and the window ID
	 */
	@SuppressWarnings("rawtypes")
	private static WindowPrivilege getWindowPrivilege(int isAdmin, Class windowId){
		UserRole userRole=null;
		try{
			userRole = userRoleMap.get(isAdmin);
		}catch(Exception e){
			log.error("Failed to get userrole from userRoleMap");
			return null;
		}
		
		return userRole.getWindowPrivilege(windowId);
	}
	
}
