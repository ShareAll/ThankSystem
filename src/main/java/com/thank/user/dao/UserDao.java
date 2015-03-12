package com.thank.user.dao;

import com.mongodb.MongoClient;
import com.thank.user.model.LoginException;
import com.thank.user.model.UserInfo;

public class UserDao extends AbstractDao<UserInfo>{
	public UserDao(MongoClient client, String dbName, Class<UserInfo> cls) {
		super(client, dbName, cls);
	}
	
	public UserInfo getByName(String userName) {
		return this.getSingleByAttr("name", userName);
	}
	public UserInfo login(String userName,String password) {
		UserInfo user=this.getSingleByAttr("name", userName);
		if(user==null) throw new LoginException("User "+userName+" does NOT exist");
		if(!user.getPassword().equals(password)) throw new LoginException("Password is wrong");
		return user;
	}
	
}
