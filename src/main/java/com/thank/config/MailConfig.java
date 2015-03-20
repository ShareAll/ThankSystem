package com.thank.config;

import java.util.Properties;

/**
 * Mail configuration
 * @author fenwang
 *
 */
public class MailConfig {
	private String smtpHost;
	private int port;
	private String userName;
	private String password;
	public MailConfig(Properties p) {
		this.smtpHost=p.getProperty("mail.hostName");
		this.port=Integer.parseInt(p.getProperty("mail.port","465"));
		this.userName=p.getProperty("mail.userName");
		this.password=p.getProperty("mail.password");
	}
	public MailConfig(String smtpHost, int port, String userName,
			String password) {
		super();
		this.smtpHost = smtpHost;
		this.port = port;
		this.userName = userName;
		this.password = password;
	}
	public String getSmtpHost() {
		return smtpHost;
	}
	public int getPort() {
		return port;
	}
	public String getUserName() {
		return userName;
	}
	public String getPassword() {
		return password;
	}
	
}
