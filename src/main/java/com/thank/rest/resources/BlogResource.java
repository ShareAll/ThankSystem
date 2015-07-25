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
import com.thank.common.model.BlogSummary;
import com.thank.rest.shared.model.WFRestException;
import com.thank.topic.dao.BlogSummaryDao;
import com.thank.utils.IDGenerator;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
/***
 * Services for Blog CRUD
 * @author fenwang
 *
 */
@Singleton
@Path("/blog" )
@Api(value = "/blog", description = "Blog api")
public class BlogResource {	      
	
	@Context private HttpServletRequest request;
	@Context private HttpServletResponse response;
	BlogSummaryDao dao=new BlogSummaryDao(null,null,BlogSummary.class);
	@GET
	@Path("list" )
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "list Blogs",
    	notes = "list Blogs"
    	)
	@ApiResponses(value = { 
		    @ApiResponse(code = 500, message = "Service exception") })
	public List<BlogSummary> listBlogs() {
		try {
			return dao.listBlogs();
		} catch(Exception e) {
			throw new WFRestException(500,e.getMessage());
		}
	}
	
	
	@POST
	@Path("create" )
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "create Blog",
    	notes = "create blog"
    	)
	@ApiResponses(value = { 
		    @ApiResponse(code = 500, message = "Service exception") })
	public List<BlogSummary> createBlog(@QueryParam("user")String user,BlogSummary blog) {
		try {
			blog.id=IDGenerator.genId();
			blog.owner=user;
			dao.save(blog);
			return dao.listBlogs();
		} catch(Exception e) {
			throw new WFRestException(500,e.getMessage());
		}
	}
	

	
	

}
