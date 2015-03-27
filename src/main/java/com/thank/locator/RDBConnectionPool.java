package com.thank.locator;

import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnection;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool.impl.StackKeyedObjectPoolFactory;

import com.thank.config.MySQLConfig;
import com.thank.config.ThankConfig;

public class RDBConnectionPool {
	public static final String VALIDATE_QUERY = "SELECT 1 FROM Dual";

	public static final int VALIDATE_QUERY_TIMEOUT = 1;
	private ObjectPool pool;

	public RDBConnectionPool(String dataSourceIdentifier) {
		try {

			/*
			 * Initialization of values from DBConnectionPool.properties file is
			 * done in helper method
			 */
			MySQLConfig loader = ThankConfig.instance().mysqlConfig;

			Class.forName(loader.getDriveName()).newInstance();

			/*
			 * Driver manager Connection Factory is chosen for making this
			 * program run as command line program without spending time on
			 * setting up datasource specific efforts.
			 */
			DriverManagerConnectionFactory drvMgrConnFactory = new DriverManagerConnectionFactory(
					loader.getConnectUrl(),
					loader.getUserName(),
					loader.getPassword());
			
			drvMgrConnFactory.createConnection();

			GenericObjectPool objectPool = new GenericObjectPool(null);
			objectPool.setMaxActive(loader.getMaxActiveObjs());
			objectPool.setMaxIdle(loader.getMaxIdleObjs());
			/*
			 * Default policy is to block the request till the pool is available
			 * with idle objects
			 */
			objectPool.setWhenExhaustedAction(getPoolExhaustedPolicy(loader.getWhenExhaust()));
			
			//runs every two minutes
			objectPool.setTimeBetweenEvictionRunsMillis(2*60*1000);

			// idle at most 10 minutes
			objectPool.setMinEvictableIdleTimeMillis(10*60*1000);
			
			objectPool.setTestWhileIdle(true);
			
			//Un-comment this when it is still not enough checking
			//objectPool.setTestOnBorrow(true);

			/*
			 * This pool factory to be used for pooling prepared statements, and
			 * to be used by PoolableConnectionFactory.
			 */
			StackKeyedObjectPoolFactory sKeyObjPoolFactory = new StackKeyedObjectPoolFactory(
					loader.getMaxIdleObjs(), loader.getInitIdelObjs());

			/*
			 * PoolableConnectionFactory for managing all Poolable connections,
			 * those are borrowed from it.
			 */

			PoolableConnectionFactory poolableConnFactory = new PoolableConnectionFactory(
					drvMgrConnFactory, objectPool, sKeyObjPoolFactory, null,
					true, true);
			
			//Check connection valudation query and timeout
			poolableConnFactory.setValidationQuery(VALIDATE_QUERY);
			poolableConnFactory.setValidationQueryTimeout(VALIDATE_QUERY_TIMEOUT);

			pool = poolableConnFactory.getPool();
			/* Set the DBEnv */

		} catch (Exception ex) {

			System.out.println("Initialization error. Msg:" + ex.getMessage());
			ex.printStackTrace();

		}
	}

	private byte getPoolExhaustedPolicy(String policy)
	{

		byte whenExhaustedAction = 0;
		if(policy.equalsIgnoreCase("grow"))
		{
			whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_GROW;
		}
		else if(policy.equalsIgnoreCase("block"))
		{
			whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_BLOCK;
		}
		else if(policy.equalsIgnoreCase("fail"))
		{
			whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_FAIL;
		}
		else
		{
			whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_BLOCK;
		}
		return whenExhaustedAction;
	}
	/**
	 * Method to borrow connection object from the available pool of connections
	 * 
	 * @return PoolableConnection
	 * @exception Exception simply throw DB exception up to the caller
	 */
	public PoolableConnection getConnection() throws Exception {
		//
		return (PoolableConnection)this.pool.borrowObject();
	}

	/**
	 * Method which releases the connection object to the pool of connections.
	 * 
	 * @param connectionObject
	 */
	public void releaseConnection(PoolableConnection connectionObject) {
		try {
			
			this.pool.returnObject(connectionObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
