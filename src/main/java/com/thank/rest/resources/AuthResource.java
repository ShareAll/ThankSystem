package com.thank.rest.resources;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.representation.Form;
import com.sun.jersey.spi.resource.Singleton;
import com.thank.user.dao.UserDao;
import com.thank.user.model.UserInfo;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Singleton
@Path("/auth" )
@Api(value = "/auth", description = "Auth api")
public class AuthResource {	      
	
	@Context private HttpServletRequest request;
	@Context private HttpServletResponse response;
	UserDao dao=new UserDao(null,null,UserInfo.class);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "get User",
    	notes = "get User",
    	response = UserTo.class
    	)
	@ApiResponses(value = { 
		    @ApiResponse(code = 404, message = "User not found") })
	public UserTo getCurrentUser() {
		ResourceUtil.authenticate(request);
		UserInfo ret=ResourceUtil.getCurUser(request);
		return new UserTo(ret);	
	}
	
	@POST
	@Path("logout")
	@ApiOperation(value = "Logout cur Session",
	notes = "Logout user"
	)
	public void logout() throws IOException {	
		ResourceUtil.logout(request);
		String contextPath=request.getContextPath();
		response.sendRedirect(contextPath+"/welcome.jsp");
	}
	
	@POST
	@Path("login" )
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Login user",
    	notes = "Login user",
    	response = UserTo.class
    	)
	@ApiResponses(value = { 
		    @ApiResponse(code = 404, message = "User not found Or Password is wrong") })
	public void login(Form loginForm) throws IOException {	
		UserInfo ret=null;
		String userName,password, forwardUrl;
		userName=loginForm.getFirst("userName");
		password=loginForm.getFirst("password");
		forwardUrl=loginForm.getFirst("forwardUrl");
		System.out.println("userName="+userName+" forwardUrl="+forwardUrl);
		String contextPath=request.getContextPath();
		try {	
			ret=dao.login(userName,password);
		} catch(Exception e) {
			ret=null;
		}
		if(ret==null) {	
			
			response.sendRedirect(contextPath+"/welcome.jsp");
			HttpSession session = request.getSession(false);
			session.setAttribute("lastLoginError", "User name and password combination is NOT valid");
			return;
		} else {
			ResourceUtil.login(request, ret);
			if(forwardUrl!=null && forwardUrl.length()>0) {	
				response.sendRedirect(forwardUrl);
			} else {
				response.sendRedirect(contextPath+"/index.jsp");
			}
		}
	}
	@POST
	@Path("signup" )
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "SignUp user",
    	notes = "SignUp user"
    	)
	@ApiResponses(value = { 
		    @ApiResponse(code = 404, message = "User not found Or Password is wrong") })
	public void signUp(Form signupForm) throws IOException {	
		UserInfo ret=null;
		String userName,password, emailAddress,forwardUrl;
		userName=signupForm.getFirst("userName");
		password=signupForm.getFirst("password");
		emailAddress=signupForm.getFirst("emailAddress");
		forwardUrl=signupForm.getFirst("forwardUrl");
		
		String contextPath=request.getContextPath();
		try {	
			ret=new UserInfo();
			ret.setName(userName);
			ret.setEmailAddress(emailAddress);
			ret.setPassword(password);
			dao.save(ret);
			ret=dao.getByName(ret.getName());
			ResourceUtil.login(request, ret);
			
		} catch(Exception e) {
			ret=null;
		}
		if(ret==null) {		
			response.sendRedirect(contextPath+"/signup.jsp");
			HttpSession session = request.getSession(false);
			session.setAttribute("lastLoginError", "User name and password combination is NOT valid");
			return;
		} else {
			ResourceUtil.login(request, ret);
			if(forwardUrl!=null && forwardUrl.length()>0) {	
				response.sendRedirect(forwardUrl);
			} else {
				response.sendRedirect(contextPath+"/index.jsp");
			}
		}
	}
	
	/*@POST
	@Path("signup" )
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "sign up user",
    	notes = "sign up user",
    	response = UserTo.class
    	)
	@ApiResponses(value = { 
		    @ApiResponse(code = 401, message = "User already exist") })
	public UserTo signup(UserTo user) {
		UserInfo dup=dao.getByName(user.name);
		if(dup!=null) throw new WFRestException(401,"User already exist");
		dup=new UserInfo();
		dup.setName(user.name);
		dup.setEmailAddress(user.emailAddress);
		dup.setPassword(user.password);
		dao.save(dup);
		UserInfo ret=dao.getByName(user.name);
		ResourceUtil.login(request, ret);
		return new UserTo(ret);
	}*/
	
	
	public static class UserTo {
		public String name;
		public String emailAddress;
		public String password=null;
		public UserTo() {
			
		}
		public UserTo(UserInfo user) {
			this.name=user.getName();
			this.emailAddress=user.getEmailAddress();
			//do NOT copy password
		}
	}


}
