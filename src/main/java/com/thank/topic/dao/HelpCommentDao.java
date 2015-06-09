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
	
	

	public long getCommentsCount(String helpId) {
		return dao.count("helpId", helpId);
	}
	public List<HelpComment> listComments(String helpId,String owner, String curUser,int privacy,String lastCommentId) {
		Query<HelpComment> query=dao.createQuery();
		query.filter("helpId",helpId);
		query.filter("id >", lastCommentId);

		if(privacy!=HelpSummary.PRIVACY_PUBLIC) {
			if(!curUser.equals(owner)) {
				query.filter("owner in ", Arrays.asList(owner,curUser));
			}
		}
		return dao.find(query).asList();	
	}
}
