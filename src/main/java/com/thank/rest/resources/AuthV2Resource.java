package com.thank.rest.resources;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.spi.resource.Singleton;
import com.thank.common.dao.ClaimableTaskUtil;
import com.thank.common.dao.UserDao;
import com.thank.common.model.ClaimSignUpVo;
import com.thank.common.model.DeviceAuthInfo;
import com.thank.common.model.DeviceSignUpVo;
import com.thank.common.model.UserInfo;
import com.thank.common.model.UserNotExistException;
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
	public UserInfo signUp(UserInfo request) throws IOException {	
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
				return this.login(request);
			} catch(Exception e) {
				throw new WFRestException(401,e.getMessage());
			}
			
		}
	}
	
	@POST
	@Path("reset" )
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Reset password",
    	notes = "Reset password"
    	)
	@ApiResponses(value = { 
		    @ApiResponse(code = 401, message = "The email address is not registered") })
	public void reset(UserInfo request) throws IOException {	
		String password, emailAddress;
		password=request.getPassword();
		emailAddress=request.getEmailAddress();
		boolean updateResult = false;
		try {
			updateResult  = dao.resetPassword(emailAddress, password);
			} catch(Exception e) {
				throw new WFRestException(401,"Email address '"+emailAddress+"' does not exist");
			}
		if (!updateResult){
			throw new WFRestException(401,"Email address '"+emailAddress+"' does not exist");
		}
	}
	
	
	@POST
	@Path("autoLogin" )
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "autoLogin",
    	notes = "Auto Login By device"
    	)
	@ApiResponses(value = { 
		    @ApiResponse(code = 401, message = "Device was NOT registered") })
	public UserInfo autoLogin(DeviceAuthInfo deviceAuth) throws IOException {
		UserInfo ret=dao.deviceLogin(deviceAuth.getDeviceId());
		if(ret==null) {
			throw new WFRestException(401,"Device was NOT registered");
		} else {
			return ret;
		}
	}
	

	
	@POST
	@Path("deviceLogin" )
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "deviceLogin",
    	notes = "Device Login"
    	)
	@ApiResponses(value = { 
		    @ApiResponse(code = 500, message = "Fail to login") })
	public UserInfo autoLogin(DeviceSignUpVo deviceSignIn) throws IOException {
		try {
			UserInfo user=dao.login(deviceSignIn.getEmailAddress(), deviceSignIn.getPassword());
			dao.deviceBind(deviceSignIn.getDeviceId(), user);
			return user;
		} catch(UserNotExistException e) {
			//auto login
			return dao.deviceSignUp(deviceSignIn);
		} catch(Exception e2) {
			throw new WFRestException(500,"Fail to login");
		}
	}
	
	

	
	@POST
	@Path("claimSignUp" )
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "claimSignUp",
    	notes = "Signup via claim"
    	)
	@ApiResponses(value = { 
		    @ApiResponse(code = 500, message = "Fail to signup") })
	public void claimSignUp(ClaimSignUpVo claimSignUp) throws IOException {
		try {
			UserInfo ret=new UserInfo();
			ret.setName(claimSignUp.getEmailAddress());
			ret.setEmailAddress(claimSignUp.getEmailAddress());
			ret.setPassword(claimSignUp.getPassword());
			dao.save(ret);
			this.login(ret);
			ClaimableTaskUtil.autoClaim(request,claimSignUp.getClaimId(),claimSignUp.getEmailAddress());
		} catch(Exception e) {
			throw new WFRestException(500,"Fail to signUp");
		}
	}


}
