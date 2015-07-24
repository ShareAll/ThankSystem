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
import com.thank.common.model.BlogSummary;
import com.thank.common.model.Counter;
import com.thank.common.model.HelpComment;
import com.thank.common.model.HelpSummary;
import com.thank.common.model.UserInfo;
import com.thank.common.model.UserSummaryVo;
import com.thank.rest.shared.model.WFRestException;
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
@Path("/blog" )
@Api(value = "/blog", description = "Blog api")
public class BlogResource {	      
	
	@Context private HttpServletRequest request;
	@Context private HttpServletResponse response;
	
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
			//TODO add real support for blog query
			List<BlogSummary> ret=new ArrayList<BlogSummary>();
			ret.add(new BlogSummary("jikarma","Topic 1"));
			ret.add(new BlogSummary("jikarma","Topic 2"));
			ret.add(new BlogSummary("jikarma","Topic 3"));
			return ret;
		} catch(Exception e) {
			throw new WFRestException(500,e.getMessage());
		}
	}
	
	

	
	

}
