package com.thank.rest.resources;

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
import com.thank.common.dao.FriendRequestDao;
import com.thank.common.dao.UserDao;
import com.thank.common.model.FriendRequestVo;
import com.thank.common.model.UserInfo;
import com.thank.common.model.UserSummaryVo;
import com.thank.rest.shared.model.WFRestException;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
/***
 * Services for friend request CRUD
 * @author fenwang
 *
 */
@Singleton
@Path("/friend" )
@Api(value = "/friend", description = "Friend api")
public class FriendRequestResource {	      
	
	@Context private HttpServletRequest request;
	@Context private HttpServletResponse response;
	UserDao userDao=new UserDao(null,null,UserInfo.class);
	FriendRequestDao dao=new FriendRequestDao(null,null,FriendRequestVo.class);
	
	
	@GET
	@Path("listByTarget" )
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "list friends waiting for accept",
    	notes = "list friends"
    	)
	@ApiResponses(value = { 
		    @ApiResponse(code = 500, message = "Service exception") })
	public List<FriendRequestVo> listByTarget(@QueryParam("user")String user) {
		try {
			//UserInfo curUser=UserContextUtil.getCurUser(request);
			//if(curUser==null) throw new RuntimeException("Please login first");	
			return dao.getRequestByTarget(user);
		} catch(Exception e) {
			throw new WFRestException(500,e.getMessage());
		}
	}

	@GET
	@Path("listByRequester" )
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "list friend request",
    	notes = "list friend request"
    	)
	@ApiResponses(value = { 
		    @ApiResponse(code = 500, message = "Service exception") })
	public List<FriendRequestVo> listByRequester(@QueryParam("user")String user) {
		try {
				
			return dao.getRequestByRequester(user);
		} catch(Exception e) {
			throw new WFRestException(500,e.getMessage());
		}
	}
	
	@GET
	@Path("list")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "get Friends",
    	notes = "get Friends",
    	response = UserSummaryVo.class
    	)
	@ApiResponses(value = { 
		    @ApiResponse(code = 404, message = "User not found") })
	public List<UserSummaryVo> getFriendList(@QueryParam("user")String user) {
		return userDao.getFriends(user);
	}
	
	@POST
	@Path("create" )
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "create Friend request",
    	notes = "create Friend request"
    	)
	@ApiResponses(value = { 
		    @ApiResponse(code = 500, message = "Service exception") })
	public FriendRequestVo createFriendRequest(@QueryParam("user")String user,FriendRequestVo friendRequest) {
		try {
			dao.save(user,friendRequest);
			return friendRequest;
		} catch(Exception e) {
			e.printStackTrace();
			throw new WFRestException(500,e.getMessage());
		}
	}
	
	@POST
	@Path("accept" )
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "accept Friend request",
    	notes = "accept Friend request"
    	)
	@ApiResponses(value = { 
		    @ApiResponse(code = 500, message = "Service exception") })
	public void acceptFriendRequest(@QueryParam("user")String user,FriendRequestVo friendRequest) {
		try {
			
			if(friendRequest.requester==null || friendRequest.target==null) {
				throw new RuntimeException("Requester and target can NOT be empty");
			}
			dao.accept(friendRequest);
		} catch(Exception e) {
			e.printStackTrace();
			throw new WFRestException(500,e.getMessage());
		}
	}
	

	

}
