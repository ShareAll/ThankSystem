package com.thank.common.dao;

import org.junit.Test;

import com.thank.common.dao.UserDao;
import com.thank.common.model.LoginException;
import com.thank.common.model.UserInfo;

public class UserDaoTest {
	@Test
	public void testSave() {
		
		
		UserDao dao=new UserDao(null,null,UserInfo.class);
		UserInfo user=dao.getSingleByAttr("name", "fenwang");
		if(user==null) user=new UserInfo();
		user.setName("fenwang");
		user.setPassword("wf");
		user.setEmailAddress("fenwang@ebay.com");
		dao.save(user);
		dao.login("fenwang","wf");
		
		try {
			dao.login("edchen", "wf");
			throw new RuntimeException("Edchen should NOT exist");
		} catch(LoginException e) {
			
		}
		try {
			dao.login("fenwang", "wf1");
			throw new RuntimeException("the password should be invalid");
		} catch(LoginException e) {
			
		}

	}
	@Test
	public void delete(){
		//String email="wangfengatustc@gmail.com";
		String email="fenwang@google.com";
		UserDao dao=new UserDao(null,null,UserInfo.class);
		dao.delete(dao.getByEmaiAddress(email));
	}
	@Test
	public void testGetByName() {
		UserDao dao=new UserDao(null,null,UserInfo.class);
		UserInfo user=dao.getSingleByAttr("name","fenwang");
		System.out.println(user);
		
	}
	
	
}
