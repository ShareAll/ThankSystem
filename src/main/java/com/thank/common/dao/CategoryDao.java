package com.thank.common.dao;

import java.util.List;

import com.mongodb.MongoClient;
import com.thank.common.model.HelpCategory;

public class CategoryDao extends AbstractDao<HelpCategory> {
	
	public CategoryDao(MongoClient client, String dbName,
			Class<HelpCategory> cls) {
		super(client, dbName, cls);
	}
	public List<HelpCategory> getAllCategories() {
		return this.find().asList();
	}
}
