package com.thank.common.dao;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import com.thank.locator.RDBConnectionLocator;




public class RDAOUtil {

	public static final String PPH_MYSQL_DATA_SOURCE = "jikarma";
	private static RDBConnectionLocator locator = RDBConnectionLocator.getInstance();

	public static void closeResource(Connection conn, String dsName,
			Statement stmt, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}

		locator.releaseConnectionToPool(conn, dsName);

	}
	
	
	public static void releaseConnection(Connection conn, Statement stmt,
			ResultSet rs, String dataSourceName, boolean resetAutoCommit) {
		try {
			if (resetAutoCommit) {
				try {
					conn.setAutoCommit(true);
				} catch (SQLException e) {
				}
			}
			closeResource(conn, dataSourceName, stmt, rs);
		} catch(Exception e) {
			
		}
	}

	public static void closeStatement(CallableStatement stmt) {

		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
	}


	
	public static void main(String[] args) throws Exception {
	

	}
}
