package com.thank.common.dao;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.thank.config.MongoConfig;
import com.thank.config.ThankConfig;

/***
 * Abstract Mongo DAO
 * @author fenwang
 *
 * @param <T>
 */
public abstract class AbstractDao<T> {
	
	protected Morphia morphia;
	protected Datastore ds;
	protected Class<T> cls;
	protected MongoConfig config;
	public AbstractDao(MongoClient client,String dbName,Class<T> cls) {
		this.config=ThankConfig.instance().mongoConfig;
		morphia=new Morphia();
		this.cls=cls;
		morphia.map(cls);
		String databaseName=dbName;
		if(databaseName==null) {
			databaseName=config.getDbName();
		}
		if (client == null){
			client = getMongoClient(null, databaseName);
		}
		this.ds=morphia.createDatastore(client, databaseName);
	}
	
	  
    public MongoClient getMongoClient(MongoClient mongoClient,String dbName) {
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
	
	public Object save(T val) {
		Key<T> key = this.ds.save(val);
		return key.getId();
	}


	public T getSingleByAttr(String attrName, Object attrVal) {
		return ds.find(this.cls).field(attrName).equal(attrVal).get();
	}

	public T getById(String id) {
		return ds.get(this.cls, id);
	}
	
	public List<T> getAttList(String filter, Object filterVal){
		//for example filter is 'xyz>=' filterVal 8.
		return ds.find(this.cls).filter(filter, filterVal).asList();
	}

	protected Query<T> getUpdateQuery(ObjectId id) {
		return ds.createQuery(this.cls).field(Mapper.ID_KEY).equal(id);
	}

	protected Datastore getDateStore() {
		return ds;
	}
	
	public void update(ObjectId id, String attrName, Object attrVal) {

		if (attrVal == null) {
			ds.update(getUpdateQuery(id), ds.createUpdateOperations(this.cls)
					.unset(attrName));
		}
		ds.update(getUpdateQuery(id),
				ds.createUpdateOperations(this.cls).set(attrName, attrVal));
	}

	public void update(ObjectId id, Map<String, Object> data) {
		if (data == null || data.size() == 0) {
			return;
		}
		UpdateOperations<T> ops = null;
		for (Map.Entry<String, Object> var : data.entrySet()) {
			if (ops == null) {
				if (var.getValue() != null) {
					ops = ds.createUpdateOperations(this.cls).set(var.getKey(),
							var.getValue());
				} else {
					ops = ds.createUpdateOperations(this.cls).unset(
							var.getKey());
				}
				continue;
			}
			if (var.getValue() != null) {
				ops = ops.set(var.getKey(), var.getValue());
			} else {
				ops = ops.unset(var.getKey());
			}
		}
		ds.update(getUpdateQuery(id), ops);
	}

	// TODO Later : will check if it is Collection then..
	public void updateArray(ObjectId id, String attrName, Object attrVal,
			boolean allowDup) {

		ds.update(
				getUpdateQuery(id),
				ds.createUpdateOperations(this.cls).add(attrName, attrVal,
						allowDup));
	}

	public void updateRemoveFirst(ObjectId id, String attrName) {
		ds.update(getUpdateQuery(id), ds.createUpdateOperations(this.cls)
				.removeFirst(attrName));
	}

	public void updateRemoveLast(ObjectId id, String attrName) {

		ds.update(getUpdateQuery(id), ds.createUpdateOperations(this.cls)
				.removeLast(attrName));
	}

	public void delete(T entity) {
		ds.delete(entity);
	}
	public void updateRemoveAll(ObjectId id, String attrName,
			List<Object> attVals) {

		ds.update(getUpdateQuery(id), ds.createUpdateOperations(this.cls)
				.removeAll(attrName, attVals));
	}
}
