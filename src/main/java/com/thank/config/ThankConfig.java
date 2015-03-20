package com.thank.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Complete ThankWeb configuration loading from config.properties
 * @author fenwang
 *
 */
public class ThankConfig {
	private volatile static ThankConfig _instance=null;
	private String PROPERTY_FILE="config.properties";
	public MailConfig mailConfig=null;
	public MongoConfig mongoConfig=null;
	
	private ThankConfig() {
		 Properties properties =new Properties();
		 InputStream inputStream = null;
		 try {
			 inputStream = ThankConfig.class.getClassLoader().getResourceAsStream(PROPERTY_FILE);
			 if (inputStream == null) {
				 throw new IOException("Fail to load "+PROPERTY_FILE);
			 }
			 properties.load(inputStream);
			 mongoConfig=new MongoConfig(properties);
			 mailConfig=new MailConfig(properties);
		 } catch (Exception ex) {
			 ex.printStackTrace();
			 System.err.println("Fail to load "+PROPERTY_FILE+",must quit");
			 System.exit(-1);
		 }
	}
	
	public static ThankConfig instance() {
		if(_instance==null) {
			synchronized(ThankConfig.class) {
				if(_instance==null) {
					_instance=new ThankConfig();
				}
			}
		}
		return _instance;
	}
	
}
