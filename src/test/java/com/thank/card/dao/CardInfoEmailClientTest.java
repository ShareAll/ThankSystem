package com.thank.card.dao;

import java.util.UUID;

import org.junit.Test;

import com.thank.common.model.CardInfo;

public class CardInfoEmailClientTest {
	CardInfoEmailClient client=new CardInfoEmailClient();
	@Test
	public void testComposeEmail() {
		CardInfo card=new CardInfo();
		card.setSubject("Thanks for your information");
		card.setContent("it is a great web site");
		card.setTemplateName("loved");
		String ret=client.composeContent(null,card);
		System.out.println(ret);
	}
	@Test
	public void testSendMail() {
		CardInfo card=new CardInfo();
		//card.setCardId("1234567");
		card.setTemplateName("loved");
		card.setContent("It is my pleasure to work in paypal.");
		card.setFromEmail("wangfengatustc@gmail.com");
		card.setRecipientEmail("fenwang@ebay.com");
		card.setSubject("Email Test");
		client.send(card);
	}
}	
