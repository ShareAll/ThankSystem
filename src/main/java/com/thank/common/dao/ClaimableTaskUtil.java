package com.thank.common.dao;

import javax.servlet.http.HttpServletRequest;

import com.thank.common.model.ClaimableTask;
import com.thank.common.model.UserInfo;
import com.thank.rest.resources.UserContextUtil;

public class ClaimableTaskUtil {
	static ClaimableTaskDao claimDao=new ClaimableTaskDao(null,null,ClaimableTask.class);
	static UserDao userDao=new UserDao(null,null,UserInfo.class);
	
	public static void createClaimTask(ClaimableTask task) {
		claimDao.save(task);
	}
	
	public static int autoClaim(HttpServletRequest request,String claimId,String emailAddress) {
		if(claimId==null || emailAddress==null) return -1;
		ClaimableTask task=claimDao.getTask(claimId, emailAddress);
		if(task==null) return -1;
		UserInfo user=userDao.getByEmaiAddress(emailAddress);
		if(user==null) return -2;		
		userDao.addScore(emailAddress,task.getScore());
		claimDao.completeClaimTask(task);
		UserContextUtil.saveInSession(request, user);
		return 0;
	}
	
}
