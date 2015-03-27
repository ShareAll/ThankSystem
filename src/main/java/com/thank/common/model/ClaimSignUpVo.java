package com.thank.common.model;

import java.io.Serializable;

public class ClaimSignUpVo implements Serializable {
	private static final long serialVersionUID = 3952426770594522640L;
	private String claimId;
	private String emailAddress;
	private String password;
	
	public ClaimSignUpVo() {
		
	} 
	public ClaimSignUpVo(String claimId, String emailAddress, String password) {
		this.claimId = claimId;
		this.emailAddress = emailAddress;
		this.password = password;
	}
	public String getClaimId() {
		return claimId;
	}
	public void setClaimId(String claimId) {
		this.claimId = claimId;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "ClaimSignUpVo [claimId=" + claimId + ", emailAddress="
				+ emailAddress + ", password=" + password + "]";
	}
	
	
}
