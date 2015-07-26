package com.thank.common.dao;

import org.junit.Test;
import org.mongodb.morphia.query.Query;

import com.thank.common.model.HelpArchive;
import com.thank.common.model.HelpComment;
import com.thank.topic.dao.HelpArchiveDao;
import com.thank.topic.dao.HelpCommentDao;

public class HelpArchiveDaoTest {
	HelpArchiveDao dao=new HelpArchiveDao(null,null,HelpArchive.class);
	@Test
	public void completeHelp() {
		String helpId="2015-07-03-10:13:295596c2b93004e12a25c72866";
		dao.completeHelp(helpId, "Test");
	}
	
	@Test
	public void deleteComment() {
		HelpCommentDao commentDao=new HelpCommentDao(null,null,HelpComment.class);
		String helpId="2015-07-03-10:13:295596c2b93004e12a25c72866";
		Query<HelpComment> q2=commentDao.createQuery().filter("helpId", helpId);
		commentDao.deleteByQuery(q2);
	}
}
