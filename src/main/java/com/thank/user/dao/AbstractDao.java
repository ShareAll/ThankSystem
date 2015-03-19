package com.thank.user.dao;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import org.bson.types.ObjectId;

public abstract class AbstractDao<T> {
	
	private final static String PROPERTY_FILE = "db.properties";
	private final static String MOMGO_HOST_NAME = "mongo-host-name";
	private final static String MONGO_PORT = "mongo-port";
	private final static String MONGO_DB = "mongo-db";
	private final static String MONGO_USER = "mongo-user";
	private final static String MONGO_PWD = "mongo-pwd";
	private final static String DEFAULT_MONGO_DB = "thankyou";
	private static Properties properties;
	static{
		properties = loadProperties() ;
	}
	
	protected Morphia morphia;
	protected Datastore ds;
	protected Class<T> cls;
	public AbstractDao(MongoClient client,String dbName,Class<T> cls) {
		morphia=new Morphia();
		this.cls=cls;
		morphia.map(cls);
		if (dbName == null){
			dbName = (String)properties.getProperty(MONGO_DB , DEFAULT_MONGO_DB);
		}
		if (client == null){
			client = getMongoClient(null, null);
		}
		this.ds=morphia.createDatastore(client, dbName);
	}
	
	  
    public MongoClient getMongoClient(MongoClient mongoClient,Properties properties) {
    	properties = properties == null? this.properties : properties;
        try {
            if (mongoClient == null){
            	int port =Integer.valueOf((String)properties.get(MONGO_PORT));
            	ServerAddress sd = new ServerAddress((String)properties.get(MOMGO_HOST_NAME), 
                		port);
            	if (properties.get(MONGO_USER)!= null){	
            		MongoCredential credential= MongoCredential.createMongoCRCredential((String)properties.get(MONGO_USER),
            			 (String)properties.get(MONGO_DB),  ((String)properties.get(MONGO_PWD)).toCharArray());
               mongoClient = new MongoClient(sd, Arrays.asList(credential));
            	}else{
            		mongoClient = new MongoClient(sd);
            	}
            }
        } catch (Exception uh) {
            
        }
        return mongoClient;
    }
	
	public Object save(T val) {
		Key<T> key=this.ds.save(val);
		return key.getId();
	}
	
	protected  Query<T> getUpdateQuery(ObjectId id){
		return ds.createQuery(this.cls).field(Mapper.ID_KEY).equal(id);
	}
	

	protected Datastore getDateStore(){
		return ds;
	}
	
	public T getSingleByAttr(String attrName,Object attrVal) {
		return ds.find(this.cls).field(attrName).equal(attrVal).get();
	}
	
	public T getById(String id) {
		return ds.get(this.cls, id);
	}
	
	
	public void update(ObjectId id, String attrName,Object attrVal) {

		if (attrVal == null){
			ds.update(getUpdateQuery(id),
					ds.createUpdateOperations(this.cls).unset(attrName));
		}
		ds.update(getUpdateQuery(id),
		ds.createUpdateOperations(this.cls).set(attrName, attrVal));
	}
	
	public void update(ObjectId id, Map<String,Object> data) {
		 if (data == null || data.size() == 0){
			 return;
		 }
		 UpdateOperations<T> ops = null;
		 for (Map.Entry<String, Object> var : data.entrySet()){
			 if (ops == null){
				 if (var.getValue()!=null){
					 ops = ds.createUpdateOperations(this.cls).set(var.getKey(), var.getValue());
				 }else{
					 ops = ds.createUpdateOperations(this.cls).unset(var.getKey());
				 }
				 continue;
			 }
			 if (var.getValue()!=null){
				 ops = ops.set(var.getKey(), var.getValue());
			 }else{
				 ops = ops.unset(var.getKey());
			 }
		 }
		ds.update(getUpdateQuery(id), ops);
	}
	
	//TODO Later : will check if it is Collection then..
	public void updateArray(ObjectId id, String attrName,Object attrVal, boolean allowDup) {

		ds.update(getUpdateQuery(id),
		ds.createUpdateOperations(this.cls).add(attrName, attrVal, allowDup));
	}
	
	public void updateRemoveFirst(ObjectId id, String attrName) {
		ds.update(getUpdateQuery(id),
		ds.createUpdateOperations(this.cls).removeFirst(attrName));
	}
	
	public void updateRemoveLast(ObjectId id, String attrName) {

		ds.update(getUpdateQuery(id),
		ds.createUpdateOperations(this.cls).removeLast(attrName));
	}
	
	public void updateRemoveAll(ObjectId id, String attrName, List<Object> attVals) {

		ds.update(getUpdateQuery(id),
		ds.createUpdateOperations(this.cls).removeAll(attrName, attVals));
	}
	
	
	
    public static  Properties loadProperties()  {
  	  
        Properties properties =   new   Properties();
        InputStream inputStream = null;
        try {

             inputStream = AbstractDao.class.getClassLoader().getResourceAsStream(PROPERTY_FILE);
            if (inputStream == null) {
            	System.err.println("Fail to load "+PROPERTY_FILE);
            	System.exit(-1);
                
            }
            properties.load(inputStream);
        } catch (Exception ex) {
            System.out.println("MongoResource: Error closing stream");
        }finally{
            if(inputStream != null){
            	try{
            inputStream.close();
            	}catch(Exception ee){}
            }
        }

        return properties;
    }
	
}
