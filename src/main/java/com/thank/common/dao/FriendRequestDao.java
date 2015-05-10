package com.thank.common.dao;

import java.util.List;

import org.mongodb.morphia.query.Query;

import com.mongodb.MongoClient;
import com.thank.common.model.FriendRequestVo;
import com.thank.common.model.UserInfo;
import com.thank.utils.IDGenerator;

public class FriendRequestDao extends AbstractDao<FriendRequestVo> {
	UserDao userDao=null;
	public FriendRequestDao(MongoClient client, String dbName,
			Class<FriendRequestVo> cls) {
		super(client, dbName, cls);
		userDao=new UserDao(client,dbName,UserInfo.class);
	}
	
	public List<FriendRequestVo> getRequestByTarget(String target) {
		Query<FriendRequestVo> query=dao.createQuery().filter("target", target);
		return dao.find(query).asList();
	}
	
	public List<FriendRequestVo> getRequestByRequester(String requester) {
		Query<FriendRequestVo> query=dao.createQuery().filter("requester", requester);
		return dao.find(query).asList();
	}
	
	public void accept(FriendRequestVo request) {
		userDao.addFriend(request.requester, request.target);
		userDao.addFriend(request.target, request.requester);
		
		Query<FriendRequestVo> query=dao.createQuery()
				.filter("target", request.target)
				.filter("requester", request.requester);
		List<FriendRequestVo> friends=dao.find(query).asList();
		for(FriendRequestVo friend:friends) {
			dao.delete(friend);
		}
		
	}
	
	public FriendRequestVo save(String requester,FriendRequestVo request) {
		UserInfo requesterInfo=userDao.getByEmaiAddress(requester);
		request.requester=requester;
		request.requester_name=requesterInfo.getName();
		
		Query<FriendRequestVo> query=dao.createQuery()
				.filter("target", request.target)
				.filter("requester", requester);
		List<FriendRequestVo> reqs=dao.find(query).asList();
		if(reqs.isEmpty()) {
			request.id=IDGenerator.genId();
			super.save(request);
		} else {
			request=reqs.iterator().next();
		}
		return request;
	}
}
