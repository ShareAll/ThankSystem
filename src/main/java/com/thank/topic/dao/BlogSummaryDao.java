package com.thank.topic.dao;

import java.util.List;

import com.mongodb.MongoClient;
import com.thank.common.dao.AbstractDao;
import com.thank.common.model.BlogSummary;


public class BlogSummaryDao extends AbstractDao<BlogSummary> {
	
	public BlogSummaryDao(MongoClient client, String dbName, Class<BlogSummary> cls) {
		super(client, dbName, cls);
	}
	public List<BlogSummary> listBlogs() {
		return dao.find().asList();
	}
	


}