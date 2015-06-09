package com.thank.rest.resources;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.codec.binary.Base64;

import com.sun.jersey.spi.resource.Singleton;
import com.thank.common.dao.ImageDao;
import com.thank.common.dao.MongoUtil;
import com.thank.rest.shared.model.WFRestException;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/***
 * Photo service
 * @author fenwang
 */
@Singleton
@Path("/photo" )
@Api(value = "/photo", description = "photo service for thank web")
public class PhotoResource {	
	@Context private HttpServletRequest request;
	@Context private HttpServletResponse response;
	private ImageDao dao=new ImageDao(MongoUtil.getMongoDb(null, null));
		
	@POST
	@Path("{key}" )
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	@ApiOperation(value = "uploadInBase64",
	   	notes = "uploadInBase64"
	)
	@ApiResponses(value = { 
		@ApiResponse(code = 500, message = "Card sending exception") })
	public void updateInBase64(@PathParam("key")String key,String rawString ) {
		try {
			InputStream stream = new ByteArrayInputStream(Base64.decodeBase64(rawString));
			dao.save(key, stream);
		} catch(Exception e) {
			throw new WFRestException(500,e.getMessage());
		}
	}

	
	@GET	
	@ApiOperation(value = "get image based on key",
    	notes = "get image based on key"
    )
	@ApiResponses(value = { 
		    @ApiResponse(code = 500, message = "Service exception") })
	public void getImage(@QueryParam("key")String key) {
		try {
			int maxAge=600;//600 seconds
			long expiry = new Date().getTime() + maxAge*1000;
			if(dao.exist(key)) {
				response.setDateHeader("Expires", expiry);
				response.addHeader("Cache-Control","no-transform,public,max-age="+maxAge);
				//ByteArrayOutputStream bos=new ByteArrayOutputStream();
				response.setContentType("image/jpeg");
				dao.writeTo(key,response.getOutputStream());
				response.getOutputStream().flush();
				//dao.writeTo(key,bos);
				
				//System.out.println(bos.toByteArray().length);
			} else {
				response.sendRedirect("../images/default.jpg");
			}
			
		} catch (IOException e) {
			throw new WFRestException(500,e.getMessage());
		}
	}

}
