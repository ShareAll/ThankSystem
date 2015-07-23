package com.thank.database;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

public class MyBatisFactoryTest {
	//create table test(id bigint not null auto_increment,name char(30) not null,lastLoginTime timestamp,primary key (id));
	
	public static class TestVo {
		public long id;
		public String name;
		public Date lastLoginTime=null;
		public TestVo(String name) {
			this.name=name;
		}
		public TestVo() {		
		}
	}
	private String format(Date d) {
		if(d==null) return "";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(d);
	}
	@Test
	public void testCreateAndQueryAndDelete() {
		SqlSession session=MyBatisFactory.instance().newSession();
		
		try {
			TestMapper mapper=session.getMapper(TestMapper.class);
			TestVo obj=new TestVo("ferry");
			mapper.insertTest(obj);
			System.out.println("Gen ID ="+obj.id);
			obj=mapper.selectTest(obj.id);
			System.out.println("Name ="+obj.name+" last Login = "+format(obj.lastLoginTime));
			obj.name="ferry1";
			Calendar cal=Calendar.getInstance();
			cal.set(2014, 10,11, 5, 16, 12);
			obj.lastLoginTime=cal.getTime();
			mapper.updateTest(obj);
			obj=mapper.selectTest(obj.id);
			System.out.println("Name change to "+obj.name+" last login="+format(obj.lastLoginTime));
			mapper.deleteTest(obj);
			//session.commit();
		} finally {
			session.close();
		}
	}
	@Test
	public void testExecute() throws SQLException {
		SqlSession session=MyBatisFactory.instance().newSession();
		try {
			String sql="update test set lastLoginTime=? where name=?";
			Calendar cal=Calendar.getInstance();
			cal.set(2016, 10,11, 5, 16, 12);
			int ret=MyBatisFactory.instance().updateSql(session, sql,cal.getTime(),"Sample");
			System.out.println("ret="+ret);
			session.commit();
			
			//session.commit();
		} finally {
			session.close();
		}
	}
}
