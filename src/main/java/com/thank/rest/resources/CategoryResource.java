package com.thank.rest.resources;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.spi.resource.Singleton;
import com.thank.common.model.HelpCategory;
import com.thank.rest.shared.model.WFRestException;
import com.thank.utils.CategoryUtil;
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
@Path("/category" )
@Api(value = "/category", description = "category api")
public class CategoryResource {	      
	
	@Context private HttpServletRequest request;
	@Context private HttpServletResponse response;

	@GET
	@Path("init" )
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "init category",
    	notes = "init category"
    	)
	@ApiResponses(value = { 
		    @ApiResponse(code = 500, message = "Service exception") })
	public List<HelpCategory> initCategoies() {
		
		try {
			String[] cats=new String[] {
					"Technology","Education","Science","Books","Computer Programming",
					"Movies","Travel","Health","Music","Design",
					"Psychology","Economics","Writing","Sports",
					"Photograph","Philosophy"
			};
			for(String cat:cats) {
				try {
					CategoryUtil.instance().createCategory(cat);
				} catch(Exception e) {
					
				}
				
			}
			return CategoryUtil.instance().getAvailableCategories();
			
		} catch(Exception e) {
			throw new WFRestException(500,e.getMessage());
		}
	}
	
	@GET
	@Path("list" )
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "list category",
    	notes = "list category"
    	)
	@ApiResponses(value = { 
		    @ApiResponse(code = 500, message = "Service exception") })
	public List<HelpCategory> listCategoies() {
		try {
			return CategoryUtil.instance().getAvailableCategories();
			
		} catch(Exception e) {
			throw new WFRestException(500,e.getMessage());
		}
	}
	
	@POST
	@Path("create" )
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "create category",
    	notes = "create category"
    	)
	@ApiResponses(value = { 
		    @ApiResponse(code = 500, message = "Service exception") })
	public Long createHelp(String categoryName) {
		try {
			return CategoryUtil.instance().createCategory(categoryName);
			
		} catch(Exception e) {
			throw new WFRestException(500,e.getMessage());
		}
	}
	
	

}
