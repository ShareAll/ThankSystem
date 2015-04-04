package com.thank.common.model;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
/**
 * @author pzou
 *
 */
@Entity("Topic")
public class Topic implements Serializable{

	private static final long serialVersionUID = -2215912467829648266L;
	@Id ObjectId id;
	private @Indexed String userEmail;
	private @Indexed String name;
	private @Indexed Date creationDate;
	private @Indexed Date startDate;
	private @Indexed int expirationDate;
	private @Indexed int accessLevel;
	private @Indexed List<String> friendEmailList;
	private @Indexed List<String> externalEmailList;
	
   public Topic() {
		
	}
	public Topic (Topic topic) {
		this.userEmail = topic.userEmail;
		this.name = topic.name;
		this.creationDate = topic.creationDate;
		this.startDate = topic.startDate;
		this.expirationDate = topic.expirationDate;
		this.accessLevel = topic.accessLevel;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public int getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(int expirationDate) {
		this.expirationDate = expirationDate;
	}
	public List<String> getFriendEmailList() {
		return friendEmailList;
	}
	public void setFriendEmailList(List<String> friendEmailList) {
		this.friendEmailList = friendEmailList;
	}
	public List<String> getExternalEmailList() {
		return externalEmailList;
	}
	public void setExternalEmailList(List<String> externalEmailList) {
		this.externalEmailList = externalEmailList;
	}
	public ObjectId getId() {
		return id;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public int getAccessLevel() {
		return accessLevel;
	}
	
	
	

}
