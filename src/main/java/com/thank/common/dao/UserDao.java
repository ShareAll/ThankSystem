package com.thank.common.dao;

import com.mongodb.MongoClient;
import com.thank.common.model.LoginException;
import com.thank.common.model.UserInfo;
/***
 * User DAO to query user info and login and signup
 * @author fenwang
 *
 */
public class UserDao extends AbstractDao<UserInfo> {
	
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
