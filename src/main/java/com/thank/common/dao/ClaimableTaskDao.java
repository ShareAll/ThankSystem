package com.thank.common.dao;

import com.mongodb.MongoClient;
import com.thank.common.model.ClaimableTask;
/***
 * ClaimableTask for user to claim karma score perform particular action
 * @author fenwang
 *
 */
public class ClaimableTaskDao extends AbstractDao<ClaimableTask> {
	public ClaimableTaskDao(MongoClient client, String dbName, Class<ClaimableTask> cls) {
		super(client, dbName, cls);
	}
	
	public ClaimableTask getTask(String claimId,String emailAddress) {
		ClaimableTask task=this.getSingleByAttr("claimId",claimId);
		if(task==null) return null;
		if(!task.getEmailAddress().equals(emailAddress)) return null;
		return task;
	}
	public void completeClaimTask(ClaimableTask task) {
		this.delete(task);
	}
}
