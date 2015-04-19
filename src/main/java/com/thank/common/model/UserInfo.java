package com.thank.common.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

import com.thank.jersey.plugin.GsonUtil;
import com.thank.rest.resources.UserContextUtil;
/***
 * User model
 * @author fenwang
 *
 */
@Entity("userInfo")
public class UserInfo implements Serializable{
	@Id ObjectId id;
	private static final long serialVersionUID = 2150083552631231099L;
	private @Indexed(unique=true) String name;
	private @Indexed String password;
	private @Indexed(unique=true) String emailAddress;
	//personal contact list
	private @Indexed Set<String> contactList = new HashSet<String>();
	private int score=1000;
	
	public UserInfo() {
		
	}
	public UserInfo(UserInfo user) {
		this.name=user.name;
		this.password=user.password;
		this.emailAddress=user.emailAddress;
		this.score=user.score;
		this.contactList.addAll(user.getContactList());
	}
	public void setContactList(Set<String> contact) {
		this.contactList.addAll(contact);
	}
	//add one contact
	public void setContact(String contact) {
		this.contactList.add(contact);
	}
	///TODO: assumeing no change outside
	public Set<String> getContactList(){
		return contactList;
	}

	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}

	public ObjectId getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("UserInfo [id=").append(id) .append(", name=").append(name).append(", password=").
				append(password).append(", emailAddress=").append( emailAddress).append(", contactList=")
				.append(contactList.toString()).append("]");
		return sb.toString();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		UserInfo other = (UserInfo) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	

	private static String jsonfy(UserInfo user, int level) {
		UserInfo info=new UserInfo(user);
		info.setPassword(null);
		if(level==0) {
			//just userName
			info.setEmailAddress(null);
		} 
		return GsonUtil.getInstance().toJson(info);
	}
	public static String getUserName(HttpServletRequest request) {
		UserInfo ret=UserContextUtil.getCurUser(request);
		if(ret==null) return "";
		else return ret.getName();
		
	}
	public static String getUserContextInScript(HttpServletRequest request) {
		UserInfo ret=UserContextUtil.getCurUser(request);
		if(ret==null) {
			return "";
		} else {
			String json=jsonfy((UserInfo)ret,1);
			StringBuilder sb=new StringBuilder();
			sb.append("<script>window.userContext="+json+"</script>");
			return sb.toString();
		} 
		
	}
	

	
}
