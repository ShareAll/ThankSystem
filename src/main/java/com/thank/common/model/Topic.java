package com.thank.common.model;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Date;
import java.util.Set;

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
	private @Indexed int expirationDays;
	private @Indexed int accessLevel;//TODO: 0 self 1 friendly  2 all
	private @Indexed int reminder;
	//Use Set to avoid duplicted
	private @Indexed Set<String> friendEmailList = new HashSet<String>();
	//TODO need to chacke if external email list does not contain friend email list.
	private @Indexed Set<String> externalEmailList = new HashSet<String>();
	
   public Topic() {
		
	}
	public Topic (Topic topic) {
		this.userEmail = topic.userEmail;
		this.name = topic.name;
		this.creationDate = topic.creationDate;
		this.startDate = topic.startDate;
		this.expirationDays = topic.expirationDays;
		this.accessLevel = topic.accessLevel;
		this.reminder = topic.reminder;
		this.friendEmailList.addAll(topic.getFriendList());
		this.externalEmailList.addAll(topic.getExternalEmailList());
	}
	
	public void setFriendList(Set list){
		//TODO: threadsafe ???
		this.friendEmailList = new HashSet<String>();
		this.friendEmailList.addAll(list);
	}
	public Set<String> getFriendList(){
		return this.friendEmailList;
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
	public int getExpirationDays() {
		return expirationDays;
	}
	public void setExpirationDays(int expirationDays) {
		this.expirationDays = expirationDays;
	}
	public Set<String> getFriendEmailList() {
		return friendEmailList;
	}
	public void setFriendEmailList(Set<String> friendEmailList) {
		this.friendEmailList.addAll(friendEmailList);
	}
	public void addFriendEmailList(String friendEmail) {
		this.friendEmailList.add(friendEmail);
	}
	public Set<String> getExternalEmailList() {
		return externalEmailList;
	}
	public void setExternalEmailList(Set<String> externalEmailList) {
		this.externalEmailList.addAll(externalEmailList);
	}
	public void addExternalEmailList(String externalEmail) {
		externalEmailList.add(externalEmail);
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
	public void setId(ObjectId id) {
		this.id = id;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public void setAccessLevel(int accessLevel) {
		this.accessLevel = accessLevel;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + accessLevel;
		result = prime * result + expirationDays;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((startDate == null) ? 0 : startDate.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Topic other = (Topic) obj;
		if (accessLevel != other.accessLevel)
			return false;
		if (expirationDays != other.expirationDays)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("Topic [id=" + id + ", userEmail=" + userEmail + ", name="
				+ name + ", creationDate=" + creationDate + ", startDate="
				+ startDate + ", expirationDays=" + expirationDays
				+ ", accessLevel=" + accessLevel + ", reminder="+reminder+", \nfriendEmailList=(");
		for (String str: friendEmailList){
			sb.append(str).append(" ");
		}
		sb.append("), \nexternalEmailList=(");
		for (String str: externalEmailList){
			sb.append(str).append(" ");
		}

		sb.append(")]");
		return sb.toString();
	}
	
	
	

}
