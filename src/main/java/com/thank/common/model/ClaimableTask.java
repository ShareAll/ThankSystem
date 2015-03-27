package com.thank.common.model;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
/***
 * User model
 * @author fenwang
 *
 */
@Entity("claimable_task")
public class ClaimableTask implements Serializable{
	private static final long serialVersionUID = 4827482669036862042L;
	@Id ObjectId id;
	private @Indexed String claimId;
	private @Indexed String emailAddress;
	private int score=1;
	public ClaimableTask() {
		
	}
	public ClaimableTask(ClaimableTask task) {
		this.claimId=task.claimId;
		this.emailAddress=task.emailAddress;
		this.score=task.score;
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
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public ObjectId getId() {
		return id;
	}
	@Override
	public String toString() {
		return "ClaimableTask [claimId=" + claimId + ", emailAddress="
				+ emailAddress + ", score=" + score + "]";
	}
	

	

	
}
