package com.thank.utils.email;

import java.util.Date;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.thank.locator.EmailSessionLocator;


public class EmailUtils {

	private static InternetAddress _oDefaultFromAddress = null;

	private static EmailUtils instance = new EmailUtils();

	public static EmailUtils getInstance() {
		return instance;
	}

	public synchronized static void setDefaultFromAddress(String fromAddress) {
		try {
			_oDefaultFromAddress = new InternetAddress(fromAddress);
		} catch (Exception eee) {
			_oDefaultFromAddress = null;
		}
	}

	public synchronized static void setDefaultFromAddress(
			InternetAddress _oAddress) {
		_oDefaultFromAddress = _oAddress;
	}

	public static InternetAddress getDefaultFromAddress() {
		return _oDefaultFromAddress;
	}

	public static boolean composeEmail(Session _oSession,
			EmailMessage _oEmailMessage) {
		return composeEmail(_oSession, _oEmailMessage, true);
	}
	
	public static boolean sendEmail(String from, List<String> to, String subject, String body) {
		EmailMessage msg = new EmailMessage(from, to, subject, body);
		
		return EmailUtils.composeEmail(
			EmailSessionLocator.getInstance()
				.getSession(), msg);

	}
	
	public static boolean sendEmail(String from, String to, String subject, String body) {
		EmailMessage msg = new EmailMessage(from, to, subject, body);
		
		return EmailUtils.composeEmail(
			EmailSessionLocator.getInstance()
				.getSession(), msg);

	}

	public static boolean composeEmail(Session _oSession,
			EmailMessage _oEmailMessage, boolean useDefaultFrom) {
		boolean ret = true;
		try {
			MimeMessage t_oMimeMessage = new MimeMessage(_oSession);
			if (_oEmailMessage.getFrom() != null) {
				try {
					t_oMimeMessage.setFrom(new InternetAddress(_oEmailMessage
							.getFrom()));
				} catch (Exception eee) {
				}
			} else {
				if (useDefaultFrom) {
					if (_oDefaultFromAddress != null) {
						try {
							t_oMimeMessage.setFrom(_oDefaultFromAddress);
						} catch (Exception eee) {
						}
					}
				}
			}
			if (_oEmailMessage.getToAddress() != null) {
				t_oMimeMessage.setRecipients(Message.RecipientType.TO,
						_oEmailMessage.getToAddress());
			}
			if (_oEmailMessage.getCcAddress() != null) {
				t_oMimeMessage.setRecipients(Message.RecipientType.CC,
						_oEmailMessage.getCcAddress());
			}
			if (_oEmailMessage.getBccAddress() != null) {
				t_oMimeMessage.setRecipients(Message.RecipientType.BCC,
						_oEmailMessage.getBccAddress());
			}
			t_oMimeMessage.setSentDate(new Date()); // set Date
			t_oMimeMessage.setSubject(_oEmailMessage.getSubject());
			if (_oEmailMessage.hasAttachment()) {
				MimeBodyPart t_oMimeBodyPart1 = new MimeBodyPart();
				t_oMimeBodyPart1.setText(_oEmailMessage.getBody());
				MimeBodyPart t_oMimeBodyPart2 = new MimeBodyPart();
				FileDataSource t_oFileDataSource = new FileDataSource(
						_oEmailMessage.getAttachment());
				t_oMimeBodyPart2.setDataHandler(new DataHandler(
						t_oFileDataSource));
				t_oMimeBodyPart2.setFileName(t_oFileDataSource.getName());
				Multipart t_oMultipart = new MimeMultipart();
				t_oMultipart.addBodyPart(t_oMimeBodyPart1);
				t_oMultipart.addBodyPart(t_oMimeBodyPart2);
				t_oMimeMessage.setContent(t_oMultipart);
			} else {
				if (_oEmailMessage.getBody() != null) {

					// t_oMimeMessage.setText(_oEmailMessage.getBody());
					t_oMimeMessage.setContent(_oEmailMessage.getBody(),
							"text/html");
				}
			}
			Transport.send(t_oMimeMessage);
		} catch (MessagingException e) {
			e.printStackTrace();
			ret = false;
		} finally {
		}
		return ret;
	}

	public static void main(String[] args){
		 sendEmail("pzou1@ebay.com", "pzou@ebay.com", "thank you", "thank");
	}
}
