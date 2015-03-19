package com.thank.user.dao;

import com.mongodb.MongoClient;
import com.thank.user.model.LoginException;
import com.thank.user.model.UserInfo;

public class UserDao extends AbstractDao<UserInfo>{
	public UserDao(MongoClient client, String dbName, Class<UserInfo> cls) {
		super(client, dbName, cls);
	}
	
	public UserInfo getByEmaiAddress(String emailAddress) {
		return this.getSingleByAttr("emailAddress", emailAddress);
	}
	public UserInfo login(String emailAddress,String password) {
		UserInfo user=this.getSingleByAttr("emailAddress", emailAddress);
		if(user==null) throw new LoginException("User "+emailAddress+" does NOT exist");
		if(!user.getPassword().equals(password)) throw new LoginException("Password is wrong");
		return user;
	}
	
}
