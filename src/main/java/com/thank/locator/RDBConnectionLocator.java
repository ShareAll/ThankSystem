package com.thank.locator;

import java.sql.Connection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.dbcp.PoolableConnection;

/*******************************
 * Name    RDBConnectionLocator
 * @author pzou
 *
 */
public class RDBConnectionLocator {

	private static final ConcurrentMap<String, RDBConnectionPool> connectionPoolMap = new ConcurrentHashMap<String, RDBConnectionPool>();

	private static RDBConnectionLocator s_instance = new RDBConnectionLocator();


	private RDBConnectionLocator() {
	}

	public static RDBConnectionLocator getInstance() {
		return s_instance;
	}


	private static RDBConnectionPool getConnectionPool(
			String dataSourceIdentifier) {

		RDBConnectionPool connectionPool = connectionPoolMap
				.get(dataSourceIdentifier);

		if (connectionPool == null) {
			try {
				connectionPool = new RDBConnectionPool(dataSourceIdentifier);

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
