package com.thank.topic.dao;

import java.util.Arrays;
import java.util.List;

import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import com.mongodb.MongoClient;
import com.thank.common.dao.AbstractDao;
import com.thank.common.model.HelpSummary;


public class HelpSummaryDao extends AbstractDao<HelpSummary> {
	
	public HelpSummaryDao(MongoClient client, String dbName, Class<HelpSummary> cls) {
		super(client, dbName, cls);
	}
	public void updateSummaryProgress(HelpSummary help) {
		if(help.completeness>100) help.completeness=100;
		UpdateOperations<HelpSummary> op = dao.createUpdateOperations().set("completeness", help.completeness);
		Query<HelpSummary> query=dao.createQuery().filter("id", help.id);
		dao.updateFirst(query,op);
	}
	public void increaseCommentCount(String helpId) {
		Query<HelpSummary> query=dao.createQuery().filter("id", helpId);
		UpdateOperations<HelpSummary> op = dao.createUpdateOperations().inc("comments", 1);
		dao.updateFirst(query, op);
	}
	public HelpSummary getSummaryById(String helpId) {	
		return (HelpSummary) dao.get(helpId);
	}
	public List<HelpSummary> listSummaryByOwner(String owner) {
		Query<HelpSummary> query=dao.createQuery().filter("owner", owner);
		return dao.find(query).asList();
	}
	public List<HelpSummary> listSummaryBySubscriber(String subscriber) {
		//Query<String> q=dao.getDatastore().createQuery(String.class).filter("value =", subscriber);
		Query<HelpSummary> query=(Query<HelpSummary>) dao.createQuery().filter("subscribers in",Arrays.asList(subscriber));
		return dao.find(query).asList();
	}
	



}