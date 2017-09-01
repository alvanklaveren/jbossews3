package com.myforum.application;

import javax.servlet.http.Cookie;

import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.WebResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
public final class CookieLogics{
	
	static{ new CookieLogics(); }

   	private static Logger log = LoggerFactory.getLogger(CookieLogics.class);

	private CookieLogics(){}
	
	public static int getCookieInt(String name){
		int cookieValue = 0;
		if(CookieLogics.getCookie(name) != null){
			if( CookieLogics.getCookie(name) == null || CookieLogics.getCookie(name).isEmpty() ){
				return cookieValue;
			}
			
			try{
				cookieValue = Integer.parseInt(CookieLogics.getCookie(name));
			}catch(NumberFormatException e){
				log.error("failed to parse cookievalue to integer: Cookie name = " + name + " - Cookie value = " + CookieLogics.getCookie(name) );
				return cookieValue;
			}
		}
		return cookieValue;
	}

	public static void setCookieInt(String name, int value){
		setCookie(name, String.valueOf(value));
	}
	
	public static String getCookie(String name){
		WebRequest 	webRequest 		= (WebRequest)RequestCycle.get().getRequest();
		Cookie 		cookie 			= webRequest.getCookie(name); 

		if(cookie == null) return "";

		return cookie.getValue();
	}

	public static void setCookie(String name, String value){
		setCookie(name, value, false);
	}

	public static void setCookieForever(String name, int value){
		setCookieForever(name, String.valueOf(value));
	}

	public static void setCookieForever(String name, String value){
		setCookie(name, value, true);
	}

	private static void setCookie(String name, String value, boolean keepForever){
		WebRequest 	webRequest 		= (WebRequest)RequestCycle.get().getRequest();
		Cookie 		cookie 			= webRequest.getCookie(name);	

		WebResponse webResponse = (WebResponse)RequestCycle.get().getResponse();

		if(cookie == null){
			cookie = new Cookie(name,value);
		}else{
			cookie.setValue(value);
		}
		
		//cookie.setDomain(AllConstants.COOKIE_DOMAIN);
		//cookie.setPath(AllConstants.COOKIE_PATH);
		
		if(keepForever){ 
			cookie.setMaxAge(60 * 60 * 24 * 365 * 10); // 10 years into the future.
		}else{
			cookie.setMaxAge(-1); // keep cookie valid until browser closes.
		}

		webResponse.addCookie( cookie );
	}

	public static void deleteCookies(String cookieNames){
		if(cookieNames == null){
			return;
		}
		for(String cookie: cookieNames.split(";")){
			CookieLogics.deleteCookie(cookie);
		}
	}
	
	public static void deleteCookie(String cookieName){
		WebRequest 	webRequest 		= (WebRequest)RequestCycle.get().getRequest();
		Cookie 		cookie 			= webRequest.getCookie(cookieName);	
		
		if(cookie == null) return;

		cookie.setMaxAge(0);
		cookie.setValue("");
		cookie.setPath(AllConstants.COOKIE_PATH);	

		WebResponse webResponse = (WebResponse)RequestCycle.get().getResponse();
		webResponse.addCookie(cookie);
	}
	
	public static String info(Cookie cookie){
		StringBuilder sb = new StringBuilder();
		sb.append("[Cookie] ");
		sb.append("Name: ");
		sb.append(cookie.getName());
		sb.append(" - Value: ");
		sb.append(cookie.getValue());
		sb.append(" - Domain: ");
		sb.append(cookie.getDomain());
		sb.append(" - Path: ");
		sb.append(cookie.getPath());
		sb.append(" - Max Age: ");
		sb.append(cookie.getMaxAge());
		sb.append(" - Version: ");
		sb.append(cookie.getVersion());
		
		return sb.toString();
	}

}
