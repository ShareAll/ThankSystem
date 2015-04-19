package com.thank.topic.dao;

import java.util.Arrays;
import java.util.List;

import org.mongodb.morphia.query.Query;

import com.mongodb.MongoClient;
import com.thank.common.dao.AbstractDao;
import com.thank.common.model.HelpComment;
import com.thank.common.model.HelpSummary;

public class HelpCommentDao extends AbstractDao<HelpComment>  {

	public HelpCommentDao(MongoClient client, String dbName,Class<HelpComment> cls) {
		super(client, dbName, cls);
	}
	
	public List<HelpComment> listComments(HelpSummary summary,String curUser) {
		Query<HelpSummary> query=dao.createQuery();
		query.filter("helpId",summary.id);
		if(!curUser.equals(summary.owner)) {
			query.filter("owner in ", Arrays.asList(summary.owner,curUser));
		}
		return dao.find(query).asList();	
	}
}
