package com.myforum.email;

import java.util.Properties;

import com.myforum.application.AllConstants;

public class HotmailMessage extends EmailMessage {

	public HotmailMessage(String to){
		super();
		setFrom(AllConstants.MY_EMAIL);
		setPassWord(AllConstants.MY_EMAIL_PASSWORD);
		setTo(to);
		setProperties();
	}
	
	private void setProperties(){
	    Properties props = getProperties();
	    props.put( "mail.smtp.auth", 			"true" );
	    props.put( "mail.smtp.starttls.enable", "true" );
	    props.put( "mail.smtp.host", 			"smtp.live.com" );
	    props.put( "mail.smtp.port", 			"587" );
	}

}
