package com.thank.common.dao;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryResults;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
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
	protected BasicDAO dao ;
	
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
		
		dao = new BasicDAO(cls,client,morphia, databaseName);

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
	
    

    public DBCollection getCollection() {
        return dao.getCollection();
    }
    
    public Query<T> createQuery(){
    	return dao.createQuery();
    }
    
    public UpdateOperations<T> createUpdateOperations() {
        return dao.createUpdateOperations();
    }
    
    public void insert(T val) {
    	dao.getCollection().insert(morphia.toDBObject(val));
    }
    
	public Object save(T val) {
		Key<T> key = dao.save(val);
		return key.getId();
	}
	
	 public Object save(final T entity, final WriteConcern wc) {
		    Key<T> key = dao.save(entity, wc);
	        return key.getId();
	    }
	 
	 public Key<T> findOneId() {
	        return dao.findOneId();
	    }

	    public Key<T> findOneId(final String key, final Object value) {
	        return dao.findOneId(key, value);
	    }

	    public Key<T> findOneId(final Query<T> query) {
	       return dao.findOneId(query);
	    }
	 
	    
	    public long count() {
	        return dao.count();
	    }

	    public long count(final String key, final Object value) {
	        return dao.count(key,value);
	    }

	    public long count(final Query<T> q) {
	        return dao.count(q);
	    }
	    public T findOne(final String key, final Object value) {
	        return (T)dao.findOne( key, value);
	    }


	    public T findOne(final Query<T> q) {
	        return (T)dao.findOne(q);
	    }
	    
	    public QueryResults<T> find() {
	        return dao.find();
	    }

	    /* (non-Javadoc)
	     * @see org.mongodb.morphia.DAO#find(org.mongodb.morphia.query.Query)
	     */
	    public QueryResults<T> find(final Query<T> q) {
	        return dao.find(q);
	    }
	    
	public T getSingleByAttr(String attrName, Object attrVal) {
		return ds.find(this.cls).field(attrName).equal(attrVal).get();
	}
	
	public boolean exists(final String key, final Object value) {
        return dao.exists(key, value);
    }

    public boolean exists(final Query<T> q) {
        return dao.exists(q);
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
	
	
	 public UpdateResults updateFirst(final Query<T> q, final UpdateOperations<T> ops) {
	        return dao.updateFirst(q, ops);
	    }

	    public UpdateResults update(final Query<T> q, final UpdateOperations<T> ops) {
	        return dao.update(q, ops);
	    }

	
	public <T> UpdateResults update(ObjectId id, String attrName, Object attrVal) {

		if (attrVal == null) {
			ds.update(getUpdateQuery(id), ds.createUpdateOperations(this.cls)
					.unset(attrName));
		}
		return ds.update(getUpdateQuery(id),
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


	public void updateRemoveAll(ObjectId id, String attrName,
			List<Object> attVals) {

		ds.update(getUpdateQuery(id), ds.createUpdateOperations(this.cls)
				.removeAll(attrName, attVals));
	}
	
	 public WriteResult delete(final T entity) {
	        return dao.delete(entity);
	    }

	    public WriteResult delete(final T entity, final WriteConcern wc) {
	        return dao.delete(entity, wc);
	    }

	    public WriteResult deleteByQuery(final Query<T> q) {
	        return dao.deleteByQuery(q);
	    }

	    public Datastore getDatastore() {
	        return dao.getDatastore();
	    }

	    public void ensureIndexes() {
	        dao.ensureIndexes();
	    }
	
}
