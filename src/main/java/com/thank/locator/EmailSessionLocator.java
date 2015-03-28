package com.thank.locator;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import com.thank.config.MailConfig;
import com.thank.config.ThankConfig;

/*******************************
 * Name    EmailEsssionLocator
 * @author pzou
 *
 */
public class EmailSessionLocator {
	
	Map<String, Session> map = new HashMap<String, Session>();
	
	private volatile static EmailSessionLocator instance;

	MailConfig config;

	private EmailSessionLocator() {
		this.config=ThankConfig.instance().mailConfig;
	    initSession(config);
	}

	public static EmailSessionLocator getInstance() {
		if(instance==null) {
			synchronized(EmailSessionLocator.class) {
				if(instance==null) {
					instance=new EmailSessionLocator();
				}
			}
		}
		return instance;
	}
	
	private Session initSession(MailConfig config){
		return initSession(config.getProperties(), config.getUserName(), config.getPassword(), null);
	}

	private Session initSession(Properties prop , String userName, String password, String lookupKey) {
		Session session;
		final String user = userName;
		final String pwd = password;
		if (lookupKey == null){
			 session = Session.getInstance(prop,
					  new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(user, pwd);
						}
					  });
			 map.put("DEFAULT", session);
		}else {
		session = Session.getDefaultInstance(prop, null);
		map.put(lookupKey, session);
		}
		return session;
	}
	
	public Session getSession() {
		return map.get("DEFAULT");
	}

	public Session getSession(Properties prop , String userName, String password,String lookupKey) {
		if (lookupKey == null){
			return map.get("DEFAULT");
		}
		Session s = map.get(lookupKey);	
		if (s == null){
			synchronized(EmailSessionLocator.class) {
			s =  initSession(prop , userName, password, lookupKey);
			}
		}
		return s;
	}
}