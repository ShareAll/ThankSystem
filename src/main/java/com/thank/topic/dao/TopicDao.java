package com.thank.topic.dao;

import java.util.HashSet;
import java.util.Date;
import java.util.Set;

import com.mongodb.MongoClient;
import com.thank.common.dao.AbstractDao;
import com.thank.common.model.Topic;


public class TopicDao extends AbstractDao<Topic> {
	
	public TopicDao(MongoClient client, String dbName, Class<Topic> cls) {
		super(client, dbName, cls);
	}
	
	public void  updateCardDeliverDate(Topic topic) {
		super.update(topic.getId(),"deliverDate" , new Date());
	}
	


	
	
	public static void main(String[] args){

		Topic topic = new Topic();
		topic.setAccessLevel(0);
		topic.setCreationDate(new Date());
		topic.setUserEmail("pzou@ebay.com");
		topic.setName("Diet Plan");
		topic.setExpirationDays(10);
		TopicDao dao = new TopicDao(null, null, Topic.class);
		Set<String> friendList = new HashSet<String>();
		friendList.add("ping_zou_2001@yahoo.com");
		friendList.add("ping_zou_20011@yahoo.com");
		topic.setFriendEmailList(friendList);
		try{
		dao.save(topic);
		}catch(com.mongodb.MongoException e){
			e.printStackTrace();

		}
		Topic topic1=dao.findOne("userEmail",topic.getUserEmail());
		System.out.println(topic1.toString());
	}
}