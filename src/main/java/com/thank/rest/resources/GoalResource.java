package com.thank.rest.resources;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.mongodb.morphia.query.Query;

import com.sun.jersey.spi.resource.Singleton;
import com.thank.card.dao.CardDao;
import com.thank.card.dao.CardInfoEmailClient;
import com.thank.common.dao.ClaimableTaskDao;
import com.thank.common.model.CardInfo;
import com.thank.common.model.ClaimableTask;
import com.thank.common.model.Topic;
import com.thank.common.model.UserInfo;
import com.thank.rest.shared.model.WFRestException;
import com.thank.topic.dao.TopicDao;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
/***
 * Services for goal CRUD
 * @author fenwang
 *
 */
@Singleton
@Path("/goal" )
@Api(value = "/goal", description = "Goal api")
public class GoalResource {	      
	
	@Context private HttpServletRequest request;
	@Context private HttpServletResponse response;
	TopicDao dao=new TopicDao(null,null,Topic.class);
	
	@GET
	@Path("list" )
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "list goal",
    	notes = "list goal"
    	)
	@ApiResponses(value = { 
		    @ApiResponse(code = 500, message = "Service exception") })
	public List<Topic> listGoal() {
		try {
			UserInfo curUser=UserContextUtil.getCurUser(request);
			if(curUser==null) throw new RuntimeException("Please login first");			
			return dao.getTopicByUser(curUser.getEmailAddress());		
		} catch(Exception e) {
			throw new WFRestException(500,e.getMessage());
		}
	}
	
	@POST
	@Path("create" )
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "create goal",
    	notes = "create goal"
    	)
	@ApiResponses(value = { 
		    @ApiResponse(code = 500, message = "Service exception") })
	public Topic createGoal(Topic topic) {
		try {
			UserInfo curUser=UserContextUtil.getCurUser(request);
			if(curUser==null) throw new RuntimeException("Please login first");
			topic.setUserEmail(curUser.getEmailAddress());
			topic.setCreationDate(new Date());
			if(topic.getExpirationDays()<=0) {
				topic.setExpirationDays(10);
			}
			dao.save(topic);
			return topic;
		} catch(Exception e) {
			throw new WFRestException(500,e.getMessage());
		}
	}
	

}
