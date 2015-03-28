package com.thank.tasks;

import java.util.List;
import java.util.TimerTask;

import com.thank.card.dao.CardDao;
import com.thank.card.dao.CardInfoEmailClient;
import com.thank.common.model.CardInfo;
import com.thank.rest.shared.model.WFRestException;

public class SendMailTask extends TimerTask{
	CardDao dao =  new CardDao(null, null, CardInfo.class);
	
	@Override
	public void run() {
		sendMail();
	}
	//TO DO sequence by now will change to async mode later.
	private void sendMail(){
		List<CardInfo> cards = dao.getSendCardList();
		for (CardInfo card: cards){
			try {
				CardInfoEmailClient client=new CardInfoEmailClient();
				client.send(card);
				dao. updateCardDeliverDate(card);
			} catch(Exception e) {
				throw new WFRestException(500,e.getMessage());
			}
		}
		
	}

}
