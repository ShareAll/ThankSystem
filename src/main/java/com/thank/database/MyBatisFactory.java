package com.thank.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.SqlRunner;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MyBatisFactory {
	private static final String CONFIG_FILE="mybatis-config.xml";
	private static volatile MyBatisFactory _instance=null;
	private SqlSessionFactory factory=null;
	public static MyBatisFactory instance() {
		if(_instance==null) {
			synchronized(MyBatisFactory.class) {
				if(_instance==null) {
					_instance=new MyBatisFactory();
				}
			}
		}
		return _instance;
	}
	private MyBatisFactory() {
		String resource = CONFIG_FILE;
		InputStream inputStream=null;
		try {
			inputStream = Resources.getResourceAsStream(resource);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
			return;
		}
		this.factory = new SqlSessionFactoryBuilder().build(inputStream);
	}
	public SqlSession newSession() {
		return this.factory.openSession();
	}
	public int updateSql(SqlSession session,String sql,Object...args) throws SQLException {
		SqlRunner runner=new SqlRunner(session.getConnection());
		return runner.update(sql, args);
		
	}
}
