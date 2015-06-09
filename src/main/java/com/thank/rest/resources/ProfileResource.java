package com.thank.rest.resources;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
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

import org.apache.commons.codec.binary.Base64;

import com.sun.jersey.spi.resource.Singleton;
import com.thank.common.dao.ImageDao;
import com.thank.common.dao.MongoUtil;
import com.thank.common.dao.UserDao;
import com.thank.common.model.UserInfo;
import com.thank.common.model.UserProfileSummaryVo;
import com.thank.common.model.UserSummaryVo;
import com.thank.rest.shared.model.WFRestException;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/***
 * Profile service
 * @author fenwang
 */
@Singleton
@Path("/profile" )
@Api(value = "/profile", description = "profile service for thank web")
public class ProfileResource {
		@Context private HttpServletRequest request;
		@Context private HttpServletResponse response;
		private ImageDao dao=new ImageDao(MongoUtil.getMongoDb(null, null));
		private UserDao userDao=new UserDao(null,null,UserInfo.class);
			
		@POST
		@Path("update" )
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		@ApiOperation(value = "update",
		   	notes = "update"
		)
		@ApiResponses(value = { 
			@ApiResponse(code = 500, message = "Photo update exception") })
		public void updateProfile(UserProfileSummaryVo profile) {
			try {
				UserInfo user=userDao.getByEmaiAddress(profile.emailAddress);
				//save photo
				if(profile.photoInBase64.startsWith("data:")) {
					int index=profile.photoInBase64.indexOf("base64,");
					if(index!=-1) {
						InputStream stream = new ByteArrayInputStream(Base64.decodeBase64(profile.photoInBase64.substring(index+7)));
						System.out.println("Save Image to "+ profile.emailAddress);
						dao.save(profile.emailAddress, stream);
					}
				} else {
					System.out.println("Image Format is not correct");
				}
				
				
				//update name
				userDao.update(user.getId(),"name", profile.name);
				
				
			} catch(Exception e) {
				throw new WFRestException(500,e.getMessage());
			}
		}
		
		public static class ProfileInfo implements Serializable {
			
			private static final long serialVersionUID = 5314334697058395656L;
			public UserSummaryVo currentUser;
			public List<UserSummaryVo> friends;
		}
		
		@GET
		@Path("getCurrentProfile" )
		@Produces(MediaType.APPLICATION_JSON)
		@ApiOperation(value = "getCurrentProfile",
		   	notes = "getCurrentProfile"
		)
		@ApiResponses(value = { 
			@ApiResponse(code = 500, message = "getCurrentProfile fail") })
		public ProfileInfo getCurrentProfile(@QueryParam("user")String user) {
			try {
				ProfileInfo ret=new ProfileInfo();
				ret.currentUser=new UserSummaryVo(userDao.getByEmaiAddress(user));
				ret.friends=userDao.getFriends(user);
				return ret;
			} catch(Exception e) {
				throw new WFRestException(500,e.getMessage());
			}
		}	
		
}
