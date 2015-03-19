package com.thank.user.model;

import java.io.Serializable;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

import com.thank.jersey.plugin.GsonUtil;
import com.thank.rest.resources.UserContextUtil;
@Entity("CardInfo")
public class CardInfo implements Serializable{

	@Id ObjectId id;
	private static final long serialVersionUID = -5780908752081049002L;
	private @Indexed String sendEmail;
	private @Indexed String recipient;
	private @Indexed String recipientEmail;
	private @Indexed Date deliverDate;
	private @Indexed String subject;
	private @Indexed String content;
	public CardInfo() {
		
	}
	public CardInfo (CardInfo card) {
		this.sendEmail = card.sendEmail;
		this.recipient = card.recipient;
		this.recipientEmail = card.recipientEmail;
		this.deliverDate = card.deliverDate;
		this.subject = card.subject;
		this.content = card.content;
	}
	

	

	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public String getSendEmail() {
		return sendEmail;
	}
	public void setSendEmail(String sendEmail) {
		this.sendEmail = sendEmail;
	}
	public String getRecipient() {
		return recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	public String getRecipientEmail() {
		return recipientEmail;
	}
	public void setRecipientEmail(String recipientEmail) {
		this.recipientEmail = recipientEmail;
	}
	public Date getDeliverDate() {
		return deliverDate;
	}
	public void setDeliverDate(Date deliverDate) {
		this.deliverDate = deliverDate;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result
				+ ((deliverDate == null) ? 0 : deliverDate.hashCode());
		result = prime * result
				+ ((recipient == null) ? 0 : recipient.hashCode());
		result = prime * result
				+ ((recipientEmail == null) ? 0 : recipientEmail.hashCode());
		result = prime * result
				+ ((sendEmail == null) ? 0 : sendEmail.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
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
		CardInfo other = (CardInfo) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (deliverDate == null) {
			if (other.deliverDate != null)
				return false;
		} else if (!deliverDate.equals(other.deliverDate))
			return false;
		if (recipient == null) {
			if (other.recipient != null)
				return false;
		} else if (!recipient.equals(other.recipient))
			return false;
		if (recipientEmail == null) {
			if (other.recipientEmail != null)
				return false;
		} else if (!recipientEmail.equals(other.recipientEmail))
			return false;
		if (sendEmail == null) {
			if (other.sendEmail != null)
				return false;
		} else if (!sendEmail.equals(other.sendEmail))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
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
