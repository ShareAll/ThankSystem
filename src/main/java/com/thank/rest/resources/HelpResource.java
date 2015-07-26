package com.thank.rest.resources;

import java.util.ArrayList;
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

import com.sun.jersey.spi.resource.Singleton;
import com.thank.common.dao.MongoCounter;
import com.thank.common.dao.UserDao;
import com.thank.common.model.Counter;
import com.thank.common.model.HelpArchive;
import com.thank.common.model.HelpComment;
import com.thank.common.model.HelpSummary;
import com.thank.common.model.UserInfo;
import com.thank.common.model.UserSummaryVo;
import com.thank.rest.shared.model.WFRestException;
import com.thank.topic.dao.HelpArchiveDao;
import com.thank.topic.dao.HelpCommentDao;
import com.thank.topic.dao.HelpSummaryDao;
import com.thank.utils.CategoryUtil;
import com.thank.utils.IDGenerator;
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
	HelpArchiveDao archiveDao=new HelpArchiveDao(null,null,HelpArchive.class);
	MongoCounter counterDao=new MongoCounter(null,null,Counter.class);
	UserDao userDao=new UserDao(null,null,UserInfo.class);
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
			
			List<HelpSummary> ret= summaryDao.listSummaryByOwner(user);
			return ret;
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
			help.id=IDGenerator.genId();
			help.owner=user;//curUser.getEmailAddress();
			help.lastCommenter=user;
			if(help.categoryId==0) {
				
				help.categoryId=CategoryUtil.instance().getCategoryId("Education").id;
			}
			summaryDao.save(help);
			return help;
		} catch(Exception e) {
			throw new WFRestException(500,e.getMessage());
		}
	}
	@POST
	@Path("updateHelp" )
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "update help",
    	notes = "update help"
    	)
	@ApiResponses(value = { 
		    @ApiResponse(code = 500, message = "Service exception") })
	public HelpSummary updateHelp(@QueryParam("user")String user,HelpSummary help) {
		try {
			//UserInfo curUser=UserContextUtil.getCurUser(request);
			//if(curUser==null) throw new RuntimeException("Please login first");
			if(help.id==null || help.id.length()==0) throw new RuntimeException("Not valid help id");
			help.owner=user;//curUser.getEmailAddress();
		
			summaryDao.save(help);
			return help;
		} catch(Exception e) {
			throw new WFRestException(500,e.getMessage());
		}
	}
	
	@POST
	@Path("updateHelpInvitation" )
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Update Help Invitation",
    	notes = "Update Help Invitation"
    	)
	@ApiResponses(value = { 
		    @ApiResponse(code = 500, message = "Service exception") })
	public HelpSummary updateHelpInvitation(@QueryParam("user")String user,HelpSummary help) {
		try {
			summaryDao.updateInvitation(help);
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
	public HelpSummary updateHelpProgress(@QueryParam("user")String user,@QueryParam("name") String userName,HelpSummary help) {
		try {
			
			help.owner=user;//curUser.getEmailAddress();
			summaryDao.updateSummaryProgress(help);
			//create Comment
			HelpComment comment=new HelpComment();
			comment.id=IDGenerator.genId();
			comment.helpId=help.id;
			comment.content="Progress Update to "+help.completeness;
			comment.owner=user;
			comment.ownerName=userName;
			commentDao.save(comment);
			return help;
		} catch(Exception e) {
			throw new WFRestException(500,e.getMessage());
		}
	}	
	
	public static class ListCommentResponse {
		public List<HelpComment> comments;
		public List<UserSummaryVo> users;
	}
	@GET
	@Path("listComment" )
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "list comment",
    	notes = "list comment"
    	)
	@ApiResponses(value = { 
		    @ApiResponse(code = 500, message = "Service exception") })
	public ListCommentResponse listComments(@QueryParam("owner")String owner,@QueryParam("user")String user,@QueryParam("helpId")String helpId,@QueryParam("privacy") int privacy,@QueryParam("lastCommentId") String lastCommentId) {
		try {
			//UserInfo curUser=UserContextUtil.getCurUser(request);
			//if(curUser==null) throw new RuntimeException("Please login first");
			//HelpSummary summary=summaryDao.getById(helpId);
			//if(summary==null) throw new RuntimeException("No help with id "+helpId);
			ListCommentResponse ret=new ListCommentResponse();
			if(lastCommentId==null || lastCommentId.length()==0) {
				//need to return help subscribers map
				HelpSummary summary=summaryDao.getById(helpId);
				List<String> userList=new ArrayList<String>();
				if(!summary.subscribers.isEmpty()) 
					userList.addAll(summary.subscribers);
				userList.add(summary.owner);
				ret.users=userDao.getUserSummaries(userList);
				
			}
			ret.comments= commentDao.listComments(helpId,owner, user,privacy,lastCommentId);
			return ret;
			
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
			comment.id=IDGenerator.genId();
			comment.pos=counterDao.getNext(comment.helpId);
			commentDao.save(comment);
			summaryDao.updateLastComment(comment.helpId,comment.owner,comment.id,comment.content,comment.pos);
			return comment;
		} catch(Exception e) {
			throw new WFRestException(500,e.getMessage());
		}
	}
	
	@POST
	@Path("voteComment" )
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "vote comment",
    	notes = "vote comment"
    	)
	@ApiResponses(value = { 
		    @ApiResponse(code = 500, message = "Service exception") })
	public HelpComment voteComment(@QueryParam("user")String user,HelpComment comment) {
		try {
			//UserInfo curUser=UserContextUtil.getCurUser(request);
			//if(curUser==null) throw new RuntimeException("Please login first");
			if(comment==null || comment.id==null) return comment;
			commentDao.voteComment(comment.id,user);
			return commentDao.getById(comment.id);
		} catch(Exception e) {
			throw new WFRestException(500,e.getMessage());
		}
	}
	
	@POST
	@Path("completeHelp" )
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Complete Help",
    	notes = "Complete Help"
    	)
	@ApiResponses(value = { 
		    @ApiResponse(code = 500, message = "Service exception") })
	public void completeHelp(@QueryParam("user")String user,HelpArchive archive) {
		try {
			archiveDao.completeHelp(archive.id,archive.conclusion);
		} catch(Exception e) {
			throw new WFRestException(500,e.getMessage());
		}
	}	
	
	

}
