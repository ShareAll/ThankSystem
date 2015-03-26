package com.thank.card.dao;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import com.thank.common.model.CardInfo;
import com.thank.config.MailConfig;
import com.thank.config.ThankConfig;
/****
 * Email Client to send card
 * @author fenwang
 *
 */
public class CardInfoEmailClient {
	MailConfig config=null;
	public CardInfoEmailClient() {
		config=ThankConfig.instance().mailConfig;
	}
	private void initSessionInfo(Email email) {
		email.setHostName(config.getSmtpHost());
		email.setSmtpPort(config.getPort());
		email.setAuthenticator(new DefaultAuthenticator(config.getUserName(),config.getPassword()));
		email.setSSLOnConnect(true);
	}
	public String composeContent(HtmlEmail email,CardInfo card) {
		String templateName=card.getTemplateName();
		InputStream in=CardInfoEmailClient.class.getClassLoader().getResourceAsStream("templates/"+templateName+".html");
		StringBuilder sb=new StringBuilder();
		byte[] buf=new byte[4096];
		int len=0;
		try {
			while((len=in.read(buf))>0) {
				sb.append(new String(buf,0,len));
			}
		} catch (IOException e) {
			try {
				in.close();
			} catch (IOException e1) {
			}
			throw new RuntimeException(e);
		}
		String ret=sb.toString();
	//	URL url;
		String cid="";
		try {
			URL curPath = getClass().getClassLoader().getResource("templates/"+card.getTemplateName()+".jpg");//CardInfoEmailClient.class.getProtectionDomain().getCodeSource().getLocation();
		//	File f=new File(curPath.getPath()+"templates/"+card.getTemplateName()+".jpg");
			cid = email.embed(curPath, "background");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		ret=ret.replaceAll("\\{\\{cid\\}\\}", cid);
		ret=ret.replaceAll("\\{\\{card\\.subject\\}\\}", StringEscapeUtils.escapeHtml4(card.getSubject()));
		ret=ret.replaceAll("\\{\\{card\\.content\\}\\}", StringEscapeUtils.escapeHtml4(card.getContent()));
		return ret;	
	}
	public void send(CardInfo card) {
		try {
			HtmlEmail email = new HtmlEmail();
			initSessionInfo(email);
			email.addTo(card.getRecipientEmail());
			email.setFrom(card.getFromEmail());
			//  email.setFrom("me@apache.org", "Me");
			email.setSubject(card.getSubject());
			email.setHtmlMsg(composeContent(email,card));
						  // send the email
			email.send();
			/*Message message = new MimeMessage(session);
			//message.setFrom(new InternetAddress));
			message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(card.getRecipientEmail()));
			message.setSubject(card.getSubject());
			
			message.setText(card.getContent());
 
			Transport.send(message);
  */
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
