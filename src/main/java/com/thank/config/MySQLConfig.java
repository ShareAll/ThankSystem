package com.thank.config;

import java.util.Properties;

public class MySQLConfig {
	private String dbName;
	private String connectUrl;
	private String driveName;
	private String userName;
	private String password;
	private int initIdelObjs;
	private int maxIdleObjs;
	private int maxActiveObjs;
	private String whenExhaust;
	
	public MySQLConfig(Properties p) {
		this.dbName=p.getProperty("mysql.DataSourceName");
		this.connectUrl=p.getProperty("mysql.connectionUri");
		this.driveName=p.getProperty("mysql.driverName");
		this.userName=p.getProperty("mysql.userName");
		this.password=p.getProperty("mysql.password");
		this.initIdelObjs=Integer.parseInt(p.getProperty("initIdleObjects","15"));
		this.maxIdleObjs=Integer.parseInt(p.getProperty("maxIdleObjects","20"));
		this.maxActiveObjs=Integer.parseInt(p.getProperty("maxActiveObjects","20"));
		this.whenExhaust=p.getProperty("whenExhaust","grow");
	}

	public String getDbName() {
		return dbName;
	}

	public String getConnectUrl() {
		return connectUrl;
	}

	public String getDriveName() {
		return driveName;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public int getInitIdelObjs() {
		return initIdelObjs;
	}

	public int getMaxIdleObjs() {
		return maxIdleObjs;
	}

	public int getMaxActiveObjs() {
		return maxActiveObjs;
	}

	public String getWhenExhaust() {
		return whenExhaust;
	}

}
