package com.thank.common.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

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
	private @Indexed String fromEmail;
	private @Indexed Date creationDate;
	private @Indexed String recipient;
	private @Indexed String recipientEmail;
	private @Indexed Date deliverDate;
	private @Indexed String subject;
	private @Indexed String content;
	private @Indexed String templateName;
	private @Indexed String cardId;

	public CardInfo() {
		
	}
	public CardInfo (CardInfo card) {
		this.fromEmail = card.fromEmail;
		this.recipient = card.recipient;
		this.recipientEmail = card.recipientEmail;
		this.deliverDate = card.deliverDate;
		this.subject = card.subject;
		this.content = card.content;
		this.templateName=card.templateName;
		this.creationDate = card.creationDate;
		this.cardId = card.cardId;
	}
	

	
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cardId == null) ? 0 : cardId.hashCode());
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result
				+ ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result
				+ ((deliverDate == null) ? 0 : deliverDate.hashCode());
		result = prime * result
				+ ((fromEmail == null) ? 0 : fromEmail.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((recipient == null) ? 0 : recipient.hashCode());
		result = prime * result
				+ ((recipientEmail == null) ? 0 : recipientEmail.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		result = prime * result
				+ ((templateName == null) ? 0 : templateName.hashCode());
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
		if (cardId == null) {
			if (other.cardId != null)
				return false;
		} else if (!cardId.equals(other.cardId))
			return false;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (deliverDate == null) {
			if (other.deliverDate != null)
				return false;
		} else if (!deliverDate.equals(other.deliverDate))
			return false;
		if (fromEmail == null) {
			if (other.fromEmail != null)
				return false;
		} else if (!fromEmail.equals(other.fromEmail))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
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
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		if (templateName == null) {
			if (other.templateName != null)
				return false;
		} else if (!templateName.equals(other.templateName))
			return false;
		return true;
	}
	public String getFromEmail() {
		return fromEmail;
	}
	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
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
	
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	

	
	

	
	public String getTemplateName() {
		return templateName;
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
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
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
		
	
	@Override
	public String toString() {
		return "CardInfo [id=" + id + ", creationDate=" + creationDate
				+ ", recipient=" + recipient
				+ ", recipientEmail=" + recipientEmail + ", deliverDate="
				+ deliverDate + ", subject=" + subject + ", content=" + content
				+ ", cardId=" + cardId + "]";
	}
	

	
}
