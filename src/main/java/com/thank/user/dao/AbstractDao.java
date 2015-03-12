package com.thank.user.dao;

import java.net.UnknownHostException;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;

public abstract class AbstractDao<T> {
	protected Morphia morphia;
	protected Datastore ds;
	protected Class<T> cls;
	public AbstractDao(MongoClient client,String dbName,Class<T> cls) {
		morphia=new Morphia();
		this.cls=cls;
		morphia.map(cls);
		
		if(client==null) {
			try {
				client=new MongoClient("127.0.0.1");
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(dbName==null || dbName.length()==0) dbName="thank"; 
		this.ds=morphia.createDatastore(client, dbName);
	}
	
	public Object save(T val) {
		Key<T> key=this.ds.save(val);
		return key.getId();
	}
	public T getSingleByAttr(String attrName,Object attrVal) {
		return ds.find(this.cls).field(attrName).equal(attrVal).get();
	}
	
}
