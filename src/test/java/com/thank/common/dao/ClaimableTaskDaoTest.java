package com.thank.common.dao;

import org.junit.Test;

import com.thank.common.model.ClaimableTask;

public class ClaimableTaskDaoTest {

	@Test
	public void testSave() {
		ClaimableTaskDao dao=new ClaimableTaskDao(null,null,ClaimableTask.class);
		ClaimableTask task=new ClaimableTask();
		task.setClaimId("1234567");
		task.setEmailAddress("fenwang@google.com");
		dao.save(task);
	}
}
