package com.thank.common.model;
/***
 * Model with subset information of user info with non private info
 * @author fenwang
 *
 */
public class UserSummaryVo {

	public String name;
	public String emailAddress;
	public String cacheId;

	public UserSummaryVo() {

	}
	public UserSummaryVo(UserInfo user) {
		this.name=user.getName();
		this.emailAddress=user.getEmailAddress();
		this.cacheId=user.getCacheId();
	}

}
