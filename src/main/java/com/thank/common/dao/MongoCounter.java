package com.thank.common.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.thank.common.model.Counter;


public class MongoCounter extends AbstractDao<Counter> {
	
	public MongoCounter(MongoClient client, String dbName,Class<Counter> cls) {
		super(client,dbName,cls);
	}
	public long getNext(String key) {
		Counter obj=this.getById(key);
		if(obj==null) {
			obj=new Counter(key,0l);
			this.insert(obj);
		}
		DBObject query=new BasicDBObject();
		query.put("_id", key);
		DBObject update=new BasicDBObject();
		DBObject fieldUpdate=new BasicDBObject();
		fieldUpdate.put("seq", 1);
		update.put("$inc",fieldUpdate);
		DBObject result=dao.getCollection().findAndModify(query, update);
		return (long) result.get("seq")+1;
	}

}
