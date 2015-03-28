package com.thank.config;

import java.util.Properties;

/**
 * Mail configuration
 * @author fenwang
 *
 */
public class MailConfig {
	private static final String DEFAULT_SMTP_SERVER = "smtp.gmail.com";
	private static final String DEFAULT_SMTP_PORT = "587";
	private static final String USERNAME = "jikarma2015@gmail.com";
	private static final String PASSWORD = "thankyou2015";
	
	private String smtpHost;
	private int port;
	private String userName;
	private String password;
	private Properties properties;
	public MailConfig(Properties p) {	
		properties= System.getProperties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", p.getProperty("mail.hostName", DEFAULT_SMTP_SERVER));
		properties.put("mail.smtp.port", p.getProperty("mail.port",DEFAULT_SMTP_PORT));
		this.userName=p.getProperty("mail.userName",USERNAME);
		this.password=p.getProperty("mail.password", PASSWORD);
	}
	
	public MailConfig(String smtpHost, int port, String userName,
			String password) {
		super();
		properties= System.getProperties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", smtpHost);
		properties.put("mail.smtp.port", port);
		this.userName = userName;
		this.password = password;
	}
	
	public Properties getProperties(){
		return properties;
	}
	public String getSmtpHost() {
		return properties.getProperty("mail.smtp.host");
	}
	public int getPort() {
		return Integer.valueOf(properties.getProperty("mail.smtp.port"));
	}
	public String getUserName() {
		return userName;
	}
	public String getPassword() {
		return password;
	}
	
}
