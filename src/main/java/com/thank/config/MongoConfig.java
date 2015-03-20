package com.thank.config;

import java.util.Properties;
/**
 * Mongo configuration
 * @author fenwang
 *
 */
public class MongoConfig {
	private String hostName;
	private int port=27017;
	private String dbName;
	private String userName;
	private String password;
	public MongoConfig(Properties p) {
		this.hostName=p.getProperty("mongo.hostName");
		this.port=Integer.parseInt(p.getProperty("mongo.port","27017"));
		this.dbName=p.getProperty("mongo.dbName","thankyou");
		this.userName=p.getProperty("mongo.userName");
		this.password=p.getProperty("mongo.password");
	}
	public MongoConfig(String hostName, int port, String dbName,String userName, String password) {
		super();
		this.hostName = hostName;
		this.port = port;
		this.dbName=dbName;
		this.userName = userName;
		this.password = password;
	}
	public String getHostName() {
		return hostName;
	}
	public int getPort() {
		return port;
	}
	public String getDbName() {
		return dbName;
	}
	public String getUserName() {
		return userName;
	}
	public String getPassword() {
		return password;
	}
	 
	
}
