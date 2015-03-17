package com.thank.user.model;

public class UserSummaryVo {

	public String name;
	public String emailAddress;

	public UserSummaryVo() {

	}
	public UserSummaryVo(UserInfo user) {
		this.name=user.getName();
		this.emailAddress=user.getEmailAddress();
	}

}
