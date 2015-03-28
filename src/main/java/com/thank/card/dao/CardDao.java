package com.thank.card.dao;


import java.util.Date;
import java.util.List;

import com.mongodb.MongoClient;
import com.thank.common.dao.AbstractDao;
import com.thank.common.model.CardInfo;
import com.thank.utils.DateUtil;

/*********************************
 * Name    CardDao
 * @author pzou
 *
 */
public class CardDao extends AbstractDao<CardInfo> {
	
	public CardDao(MongoClient client, String dbName, Class<CardInfo> cls) {
		super(client, dbName, cls);
	}
	
	public void  updateCardDeliverDate(CardInfo card) {
		super.update(card.getId(),"deliverDate" , new Date());
	}
	
	public List<CardInfo> getSendCardList(){
		//for example filter is 'xyz>=' filterVal 8.
		return super.getAttList("deliverDate", null);
	}
	

	
	
	public static void main(String[] args){

		CardInfo card = new CardInfo();
		card.setFromEmail("pzou@ebay.com");
		card.setRecipientEmail("ping_zou_2001@yahoo.com");
		card.setSubject("Test1");
		card.setContent("Test");
		card.setCreationDate(new Date());
		CardDao dao = new CardDao(null, null, CardInfo.class);
		dao.save(card);
		System.out.println(card.toString());
		dao.updateCardDeliverDate(card);
		System.out.println(card.toString());
		List<CardInfo> list=dao.getAttList("deliverDate", null);
		for (CardInfo c : list){
			System.out.println("  "+card.toString());
		}
	}
}