package com.thank.common.dao;

import java.util.List;

import org.junit.Test;

import com.thank.common.model.HelpSummary;
import com.thank.topic.dao.HelpSummaryDao;

public class HelpSummaryDaoTest {
	@Test
	public void testQuerySubscriber() {
		HelpSummaryDao summaryDao=new HelpSummaryDao(null,null,HelpSummary.class);
		List<HelpSummary> results=summaryDao.listSummaryBySubscriber("pzou@ebay.com");
		for(HelpSummary help:results) {
			System.out.println(help.title);
		}
	}
}
