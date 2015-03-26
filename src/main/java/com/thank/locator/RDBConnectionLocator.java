package com.thank.locator;

import java.sql.Connection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.dbcp.PoolableConnection;


public class RDBConnectionLocator {

	private static final ConcurrentMap<String, DBConnectionPool> connectionPoolMap = new ConcurrentHashMap<String, DBConnectionPool>();

	private static RDBConnectionLocator s_instance = new RDBConnectionLocator();


	private RDBConnectionLocator() {
	}

	public static RDBConnectionLocator getInstance() {
		return s_instance;
	}


	private static DBConnectionPool getConnectionPool(
			String dataSourceIdentifier) {

		DBConnectionPool connectionPool = connectionPoolMap
				.get(dataSourceIdentifier);

		if (connectionPool == null) {
			try {
				connectionPool = new DBConnectionPool(dataSourceIdentifier);

				connectionPoolMap.put(dataSourceIdentifier, connectionPool);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return connectionPool;
	}

	public Connection getConnectionFromPool(String dataSourceIdentifier)
			throws Exception {

		return getConnectionPool(dataSourceIdentifier.toUpperCase())
				.getConnection();

	}

	public void releaseConnectionToPool(Connection connection,
			String dataSourceIdentifier) {

		getConnectionPool(dataSourceIdentifier.toUpperCase())
				.releaseConnection((PoolableConnection) connection);
	}


}
