package com.thank.topic.dao;

import org.mongodb.morphia.query.Query;

import com.mongodb.MongoClient;
import com.thank.common.dao.AbstractDao;
import com.thank.common.model.HelpArchive;
import com.thank.common.model.HelpComment;
import com.thank.common.model.HelpSummary;


public class HelpArchiveDao extends AbstractDao<HelpArchive>{
	HelpSummaryDao summaryDao=null;
	HelpCommentDao commentDao=null;
	public HelpArchiveDao(MongoClient client, String dbName,
			Class<HelpArchive> cls) {
		super(client, dbName, cls);
		summaryDao=new HelpSummaryDao(client,dbName,HelpSummary.class);
		commentDao=new HelpCommentDao(client,dbName,HelpComment.class);
	}

	public void completeHelp(String helpId,String conclusion) {
		HelpSummary summary=summaryDao.getById(helpId);
		if(summary==null) return;
		HelpArchive archive=new HelpArchive(summary);
		archive.conclusion=conclusion;
		dao.save(archive);
		summaryDao.delete(summary);
		Query<HelpComment> q2=commentDao.createQuery().filter("helpId", helpId);		
		commentDao.deleteByQuery(q2);
	}
}
