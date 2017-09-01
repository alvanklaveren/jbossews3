package com.myforum.email;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;


public class MailAuthenticator extends Authenticator {
     String user;
     String pw;

     public MailAuthenticator (EmailMessage emailMessage)
     {
        super();
        user = emailMessage.getFrom();
        pw = emailMessage.getPassWord();
     }
    public PasswordAuthentication getPasswordAuthentication()
    {
       return new PasswordAuthentication(user, pw);
    }
}