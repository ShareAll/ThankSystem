package com.thank.common.dao;

import org.mongodb.morphia.query.UpdateResults;

import com.mongodb.MongoClient;
import com.thank.common.model.CardInfo;
import com.thank.common.model.DeviceAuthInfo;
import com.thank.common.model.DeviceSignUpVo;
import com.thank.common.model.LoginException;
import com.thank.common.model.UserInfo;
import com.thank.common.model.UserNotExistException;
/***
 * User DAO to query user info and login and signup
 * @author fenwang
 *
 */
public class UserDao extends AbstractDao<UserInfo> {
	class DeviceAuthDao extends AbstractDao<DeviceAuthInfo> {
		public DeviceAuthDao(MongoClient client, String dbName,Class<DeviceAuthInfo> cls) {
			super(client, dbName, cls);
		}
		public DeviceAuthInfo getByDeviceId(String deviceId) {
			return this.findOne("deviceId", deviceId);
		}
		
	}
	DeviceAuthDao deviceDao=null;
	public UserDao(MongoClient client, String dbName, Class<UserInfo> cls) {
		super(client, dbName, cls);
		deviceDao=new DeviceAuthDao(client,dbName,DeviceAuthInfo.class);
	}
	
	public UserInfo getByEmaiAddress(String emailAddress) {
		return this.findOne("emailAddress", emailAddress);
	}

	public UserInfo deviceLogin(String deviceId) {
		if(deviceId==null || deviceId.length()==0) return null;
		DeviceAuthInfo deviceAuth=deviceDao.getByDeviceId(deviceId);
		if(deviceAuth==null) return null;
		UserInfo user=this.findOne("emailAddress", deviceAuth.getEmailAddress());
		return user;
	}
	public void deviceBind(String deviceId,UserInfo user) {
		DeviceAuthInfo deviceAuth=new DeviceAuthInfo(deviceId,user.getEmailAddress());
		deviceDao.save(deviceAuth);
	}
	public UserInfo deviceSignUp(DeviceSignUpVo deviceSignUp) {
		UserInfo ret=new UserInfo();
		ret.setName(deviceSignUp.getName());
		ret.setEmailAddress(deviceSignUp.getEmailAddress());
		ret.setPassword(deviceSignUp.getPassword());
		this.save(ret);
		deviceBind(deviceSignUp.getDeviceId(),ret);
		return this.findOne("emailAddress",deviceSignUp.getEmailAddress());
		
	}
	public UserInfo addScore(String emailAddress,int delta) {
		UserInfo user=this.findOne("emailAddress", emailAddress);
		if(user==null) return null;
		int newScore=user.getScore()+delta;
		if(newScore<0) newScore=0;
		this.update(user.getId(), "score", newScore);
		return this.findOne("emailAddress", emailAddress);
	}
	
	public UserInfo login(String emailAddress,String password) {
		UserInfo user=this.findOne("emailAddress", emailAddress);
		if(user==null) throw new UserNotExistException("User "+emailAddress+" does NOT exist");
		if(!user.getPassword().equals(password)) throw new LoginException("Password is wrong");
		return user;
	}
	
	public boolean resetPassword(String emailAddress,String password) {
		UserInfo user=this.findOne("emailAddress", emailAddress);
		if(user==null) throw new UserNotExistException("User "+emailAddress+" does NOT exist");
		return this.update(user.getId(),"password", password).getUpdatedExisting();

	}
	
//	public static void main(String[] args){
//		UserInfo userInfo = new UserInfo();
//		userInfo.setEmailAddress("pzou1@ebay.com");
//		userInfo.setPassword("test");
//		userInfo.setScore(100);
//		UserDao dao = new UserDao(null, null, UserInfo.class);
//		try{
//		dao.save(userInfo);
//		}catch(com.mongodb.MongoException e){
//			e.printStackTrace();
//			UserInfo userInfo1=dao.findOne("emailAddress",userInfo.getEmailAddress());
//			dao.update(userInfo1.getId(), "password", "test2");
//		}
//
//	}
}
