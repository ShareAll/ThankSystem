package com.thank.common.model;

import java.io.Serializable;

/***
 * Model with subset information of user info with non private info
 * @author fenwang
 *
 */
public class UserSummaryVo implements Serializable {


	private static final long serialVersionUID = 6439147254654038061L;
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
