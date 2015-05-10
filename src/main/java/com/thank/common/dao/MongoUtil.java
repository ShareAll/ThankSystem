package com.thank.common.dao;

import java.util.Arrays;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.thank.config.MongoConfig;
import com.thank.config.ThankConfig;

public class MongoUtil {
	public static MongoClient getMongoClient(MongoClient mongoClient,String dbName) {
		MongoConfig config=ThankConfig.instance().mongoConfig;
		if(dbName==null) dbName=config.getDbName();
		try {
			if (mongoClient == null){
				ServerAddress sd = new ServerAddress(config.getHostName(), config.getPort());
				if(config.getUserName()!=null) {
					MongoCredential credential= MongoCredential.createMongoCRCredential(config.getUserName(), dbName,  config.getPassword().toCharArray());
					mongoClient = new MongoClient(sd, Arrays.asList(credential));
				} else {
					mongoClient = new MongoClient(sd);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mongoClient;
	}
	public static DB getMongoDb(MongoClient client,String dbName) {
		MongoConfig config=ThankConfig.instance().mongoConfig;
		if(dbName==null) dbName=config.getDbName();
		return getMongoClient(client,dbName).getDB(dbName);
	}
	DB db;
	public MongoUtil(MongoClient client,String dbName) {
		if (client == null){
			client = getMongoClient(null, dbName);
			
		}
		this.db=client.getDB(dbName);
		
		
	}
	public void runCommand(String cmd) {
		db.command(cmd);
	}
	
}
