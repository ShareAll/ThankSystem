package com.thank.rest.resources;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.spi.resource.Singleton;
import com.thank.common.dao.UserDao;
import com.thank.common.model.UserInfo;
import com.thank.common.model.UserSummaryVo;
import com.thank.rest.shared.model.WFRestException;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
/***
 * Auth resource
 * @author fenwang
 *
 */
@Singleton
@Path("/auth2" )
@Api(value = "/auth2", description = "Auth api")
public class AuthV2Resource {	      
	
	@Context private HttpServletRequest request;
	@Context private HttpServletResponse response;
	UserDao dao=new UserDao(null,null,UserInfo.class);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "get User",
    	notes = "get User",
    	response = UserSummaryVo.class
    	)
	@ApiResponses(value = { 
		    @ApiResponse(code = 404, message = "User not found") })
	public UserSummaryVo getCurrentUser() {
		UserContextUtil.authenticate(request);
		UserInfo ret=UserContextUtil.getCurUser(request);
		return new UserSummaryVo(ret);	
	}
	
	@POST
	@Path("logout")
	@ApiOperation(value = "Logout cur Session",
	notes = "Logout user"
	)
	public void logout() throws IOException {	
		UserContextUtil.logout(request);
		String contextPath=request.getContextPath();
		response.sendRedirect(contextPath+"/welcome.jsp");
	}
	
	@POST
	@Path("login" )
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Login user",
    	notes = "Login user",
    	response = UserInfo.class
    	)
	@ApiResponses(value = { 
		    @ApiResponse(code = 404, message = "User not found Or Password is wrong") })
	public  UserInfo login(UserInfo user) throws IOException {	
		UserInfo ret=null;
		String authErrorMsg="User not found Or Password is wrong";
		if(request==null) throw new WFRestException(404,authErrorMsg);
		String emailAddress=user.getEmailAddress();
		String password=user.getPassword();
		
		if(emailAddress==null || password==null) throw new WFRestException(404,authErrorMsg);
		try {
			ret=dao.login(emailAddress,password);
			UserContextUtil.saveInSession(request, ret);
			return ret;
		} catch(Exception e) {
			throw new WFRestException(404,authErrorMsg);
		}
	}
	@POST
	@Path("signup" )
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "SignUp user",
    	notes = "SignUp user"
    	)
	@ApiResponses(value = { 
		    @ApiResponse(code = 401, message = "The email address already registered") })
	public void signUp(UserInfo request) throws IOException {	
		UserInfo ret=null;
		String userName,password, emailAddress;
		userName=request.getName();
		password=request.getPassword();
		emailAddress=request.getEmailAddress();
		if(dao.getByEmaiAddress(emailAddress)!=null)  {
			throw new WFRestException(401,"The email address already registered");
		} else {
			try {
				ret=new UserInfo();
				ret.setName(userName);
				ret.setEmailAddress(emailAddress);
				ret.setPassword(password);
				dao.save(ret);
				this.login(request);
			} catch(Exception e) {
				throw new WFRestException(401,"UserName already exist");
			}
			
		}
	}
	
	


}
