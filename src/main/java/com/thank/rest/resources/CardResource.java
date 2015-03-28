package com.thank.rest.resources;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.spi.resource.Singleton;
import com.thank.card.dao.CardDao;
import com.thank.card.dao.CardInfoEmailClient;
import com.thank.common.dao.ClaimableTaskDao;
import com.thank.common.model.CardInfo;
import com.thank.common.model.ClaimableTask;
import com.thank.rest.shared.model.WFRestException;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
/***
 * Services for send card
 * @author fenwang
 *
 */
@Singleton
@Path("/card" )
@Api(value = "/card", description = "Card api")
public class CardResource {	      
	
	@Context private HttpServletRequest request;
	@Context private HttpServletResponse response;
	ClaimableTaskDao dao=new ClaimableTaskDao(null,null,ClaimableTask.class);
	CardDao cardDao = new CardDao(null,null,CardInfo.class);
	
	@POST
	@Path("send" )
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "send card",
    	notes = "send card"
    	)
	@ApiResponses(value = { 
		    @ApiResponse(code = 500, message = "Card sending exception") })
	public void sendCard(CardInfo card) {
		try {
			card.setCardId(UUID.randomUUID().toString());
			cardDao.save(card);
			ClaimableTask task=new ClaimableTask();
			task.setClaimId(card.getCardId());
			task.setEmailAddress(card.getRecipientEmail());
			dao.save(task);
		} catch(Exception e) {
			throw new WFRestException(500,e.getMessage());
		}
	}
	


}
