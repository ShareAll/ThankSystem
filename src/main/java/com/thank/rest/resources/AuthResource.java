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
import com.thank.common.dao.UserDao;
import com.thank.common.model.UserInfo;
import com.thank.rest.shared.model.WFRestException;
import com.thank.rest.shared.model.WFRestRedirectModel;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
/***
 * No longer use
 * @author fenwang
 *
 */
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
		UserContextUtil.authenticate(request);
		UserInfo ret=UserContextUtil.getCurUser(request);
		return new UserTo(ret);	
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
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Login user",
    	notes = "Login user",
    	response = WFRestRedirectModel.class
    	)
	@ApiResponses(value = { 
		    @ApiResponse(code = 404, message = "User not found Or Password is wrong") })
	public  WFRestRedirectModel login(Form loginForm) throws IOException {	
		UserInfo ret=null;
		String userName,password, forwardUrl,format;
		userName=loginForm.getFirst("userName");
		password=loginForm.getFirst("password");
		forwardUrl=loginForm.getFirst("forwardUrl");
		format=loginForm.getFirst("format");
		
		String contextPath=request.getContextPath();
		try {	
			ret=dao.login(userName,password);
		} catch(Exception e) {
			ret=null;
		}
		if(ret==null) {
			WFRestRedirectModel loc=new WFRestRedirectModel(contextPath+"/welcome.jsp","User name and password combination is NOT valid");	
			if(format!=null && "json".equals(format)) {
				throw new WFRestException(404,loc.lastError);
			} else {
				response.sendRedirect(loc.location);
				HttpSession session = request.getSession(false);
				session.setAttribute("lastLoginError", loc.lastError);
				return loc;				
			}
		} else {
			UserContextUtil.saveInSession(request, ret);
			WFRestRedirectModel loc=new WFRestRedirectModel();
			String url=(forwardUrl!=null && forwardUrl.length()>0)?forwardUrl:contextPath+"/index.jsp";
			if(format!=null && "json".equals(format)) {
				loc.location=url;
			} else {
				response.sendRedirect(url);
			}
			return loc;
		}
	}
	@POST
	@Path("signup" )
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
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
			ret=dao.getByEmaiAddress(emailAddress);
			UserContextUtil.saveInSession(request, ret);
			
		} catch(Exception e) {
			ret=null;
		}
		if(ret==null) {		
			response.sendRedirect(contextPath+"/signup.jsp");
			HttpSession session = request.getSession(false);
			session.setAttribute("lastLoginError", "User name and password combination is NOT valid");
			return;
		} else {
			UserContextUtil.saveInSession(request, ret);
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
