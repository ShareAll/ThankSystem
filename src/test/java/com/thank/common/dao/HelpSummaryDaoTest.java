package com.thank.common.dao;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.google.gson.Gson;
import com.thank.common.model.HelpSummary;
import com.thank.topic.dao.HelpSummaryDao;
import com.thank.utils.IDGenerator;

public class HelpSummaryDaoTest {
	@Test
	public void testCreateSummary() {
		HelpSummaryDao summaryDao=new HelpSummaryDao(null,null,HelpSummary.class);
		HelpSummary help=new HelpSummary();
		help.id=IDGenerator.genId();
		help.owner="fenwang@ebay.com";
		help.title="Public Test";
		
		summaryDao.save(help);
	}
	@Test
	public void testQueryByOwner() {
		HelpSummaryDao summaryDao=new HelpSummaryDao(null,null,HelpSummary.class);
		List<HelpSummary> results=summaryDao.listSummaryByOwner("fenwang@ebay.com");
		Gson g=new Gson();
		for(HelpSummary help:results) {
			System.out.println(g.toJson(help));
		}
		
	}
	@Test
	public void updateLastComment() {
		String commenter="fenwang@ebay.com";
		String commentId="2015-06-07-22:19:11557525cf30045725879ce5fd";
		String commentMessage="it is a test";
		String helpId="2015-05-25-17:35:585563bfee30041d301b86a5ef";
		HelpSummaryDao summaryDao=new HelpSummaryDao(null,null,HelpSummary.class);
		summaryDao.updateLastComment(helpId, commenter,commentId, commentMessage,0);
	}
	
	@Test
	public void testQuerySubscriber() {
		HelpSummaryDao summaryDao=new HelpSummaryDao(null,null,HelpSummary.class);
		List<HelpSummary> results=summaryDao.listSummaryBySubscriber("pzou@ebay.com");
		for(HelpSummary help:results) {
			System.out.println(help.title);
		}
	}
}
