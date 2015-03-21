package com.thank.utils.email;


import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

public class EmailSessionLocator {
	private static final String DEFAULT_SMTP_SERVER = "smtp.gmail.com";
	private static final String DEFAULT_SMTP_PORT = "587";
	private static final String USERNAME = "jikarma2015@gmail.com";
	private static final String PASSWORD = "thankyou2015";
	private static final Properties defaultProp;
	static {
		defaultProp= System.getProperties();
		defaultProp.put("mail.smtp.auth", "true");
		defaultProp.put("mail.smtp.starttls.enable", "true");
		defaultProp.put("mail.smtp.host", DEFAULT_SMTP_SERVER);
		defaultProp.put("mail.smtp.port", DEFAULT_SMTP_PORT);
	}
	
	Map<String, Session> map = new HashMap<String, Session>();
	
	private static EmailSessionLocator instance;
	
	Session defaultSession;

	private EmailSessionLocator() {
		defaultSession = initSession(null, null);
	}

	public synchronized static EmailSessionLocator getInstance() {
		if (instance == null)
			instance = new EmailSessionLocator();

		return instance;
	}

	private Session initSession(String smtpServer, String port) {
		Session session;
		if (smtpServer == null){
			 session = Session.getInstance(defaultProp,
					  new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(USERNAME, PASSWORD);
						}
					  });
			 map.put("DEFAULT", session);
		}else {
		Properties t_oProps = System.getProperties();
		t_oProps.put("mail.smtp.host", smtpServer);
		t_oProps.put("mail.smtp.port", port);
		session = Session.getDefaultInstance(t_oProps, null);
		map.put(smtpServer, session);
		}
		return session;
	}
	
	public Session getSession() {
		return defaultSession;
	}

	public Session getSession(String smtpServer, String port) {
		if (smtpServer == null){
			return map.get("DEFAULT");
		}
		Session s = map.get(smtpServer);		
		if (s == null) {
			s = initSession(smtpServer, port);
		}	
		return s;
	}
}