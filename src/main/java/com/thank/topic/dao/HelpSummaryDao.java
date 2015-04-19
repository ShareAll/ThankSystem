package com.thank.topic.dao;

import java.util.List;

import org.mongodb.morphia.query.Query;

import com.mongodb.MongoClient;
import com.thank.common.dao.AbstractDao;
import com.thank.common.model.HelpSummary;


public class HelpSummaryDao extends AbstractDao<HelpSummary> {
	
	public HelpSummaryDao(MongoClient client, String dbName, Class<HelpSummary> cls) {
		super(client, dbName, cls);
	}
	public HelpSummary getSummaryById(String helpId) {	
		return (HelpSummary) dao.get(helpId);
	}
	public List<HelpSummary> listSummaryByOwner(String owner) {
		Query<HelpSummary> query=dao.createQuery().filter("owner", owner);
		return dao.find(query).asList();
	}
	



}