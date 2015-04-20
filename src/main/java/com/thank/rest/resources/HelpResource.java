package com.thank.rest.resources;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.bson.types.ObjectId;

import com.sun.jersey.spi.resource.Singleton;
import com.thank.common.model.HelpComment;
import com.thank.common.model.HelpSummary;
import com.thank.rest.shared.model.WFRestException;
import com.thank.topic.dao.HelpCommentDao;
import com.thank.topic.dao.HelpSummaryDao;
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
@Path("/help" )
@Api(value = "/help", description = "Help api")
public class HelpResource {	      
	
	@Context private HttpServletRequest request;
	@Context private HttpServletResponse response;
	HelpSummaryDao summaryDao=new HelpSummaryDao(null,null,HelpSummary.class);
	HelpCommentDao commentDao=new HelpCommentDao(null,null,HelpComment.class);
	@GET
	@Path("list" )
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "list help",
    	notes = "list help"
    	)
	@ApiResponses(value = { 
		    @ApiResponse(code = 500, message = "Service exception") })
	public List<HelpSummary> listHelp(@QueryParam("user")String user) {
		try {
			//UserInfo curUser=UserContextUtil.getCurUser(request);
			//if(curUser==null) throw new RuntimeException("Please login first");	
			return summaryDao.listSummaryByOwner(user);
		} catch(Exception e) {
			throw new WFRestException(500,e.getMessage());
		}
	}
	@GET
	@Path("listBySubscriber" )
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "list help by subscriber",
    	notes = "list help by subscriber"
    	)
	@ApiResponses(value = { 
		    @ApiResponse(code = 500, message = "Service exception") })
	public List<HelpSummary> listHelpBySubscriber(@QueryParam("user")String user) {
		try {
			//UserInfo curUser=UserContextUtil.getCurUser(request);
			//if(curUser==null) throw new RuntimeException("Please login first");	
			return summaryDao.listSummaryBySubscriber(user);
		} catch(Exception e) {
			throw new WFRestException(500,e.getMessage());
		}
	}
	
	@POST
	@Path("createHelp" )
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "create help",
    	notes = "create help"
    	)
	@ApiResponses(value = { 
		    @ApiResponse(code = 500, message = "Service exception") })
	public HelpSummary createHelp(@QueryParam("user")String user,HelpSummary help) {
		try {
			//UserInfo curUser=UserContextUtil.getCurUser(request);
			//if(curUser==null) throw new RuntimeException("Please login first");
			help.id=ObjectId.get().toHexString();
			help.owner=user;//curUser.getEmailAddress();
		
			summaryDao.save(help);
			return help;
		} catch(Exception e) {
			throw new WFRestException(500,e.getMessage());
		}
	}
	
	@POST
	@Path("updateHelpProgress" )
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Update Help progress",
    	notes = "Update Help progress"
    	)
	@ApiResponses(value = { 
		    @ApiResponse(code = 500, message = "Service exception") })
	public HelpSummary updateHelpProgress(@QueryParam("user")String user,HelpSummary help) {
		try {
			
			help.owner=user;//curUser.getEmailAddress();
			summaryDao.increaseSummaryProgress(help);
			return help;
		} catch(Exception e) {
			throw new WFRestException(500,e.getMessage());
		}
	}	
	
	@GET
	@Path("listComment" )
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "list comment",
    	notes = "list comment"
    	)
	@ApiResponses(value = { 
		    @ApiResponse(code = 500, message = "Service exception") })
	public List<HelpComment> listComments(@QueryParam("user")String user,@QueryParam("helpId")String helpId) {
		try {
			//UserInfo curUser=UserContextUtil.getCurUser(request);
			//if(curUser==null) throw new RuntimeException("Please login first");
			HelpSummary summary=summaryDao.getById(helpId);
			if(summary==null) throw new RuntimeException("No help with id "+helpId);
			return commentDao.listComments(summary, user);
			
		} catch(Exception e) {
			throw new WFRestException(500,e.getMessage());
		}
	}
	
	@POST
	@Path("createComment" )
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "create comment",
    	notes = "create comment"
    	)
	@ApiResponses(value = { 
		    @ApiResponse(code = 500, message = "Service exception") })
	public HelpComment createComment(@QueryParam("user")String user,HelpComment comment) {
		try {
			//UserInfo curUser=UserContextUtil.getCurUser(request);
			//if(curUser==null) throw new RuntimeException("Please login first");
			comment.owner=user;
			comment.createTime=new Date();
			comment.id=ObjectId.get().toHexString();
			commentDao.save(comment);
			summaryDao.increaseCommentCount(comment.helpId);
			return comment;
		} catch(Exception e) {
			throw new WFRestException(500,e.getMessage());
		}
	}
	

	
	

}
