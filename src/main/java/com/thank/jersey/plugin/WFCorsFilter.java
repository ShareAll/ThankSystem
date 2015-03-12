package com.thank.jersey.plugin;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

public class WFCorsFilter implements ContainerResponseFilter {

	@Override
	public ContainerResponse filter(ContainerRequest req, ContainerResponse resp) {
		ResponseBuilder respBuilder = Response.fromResponse(resp.getResponse());
		respBuilder.header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET,PUT,POST, OPTIONS");
 
        String reqHead = req.getHeaderValue("Access-Control-Request-Headers");
 
        if(null != reqHead && !reqHead.equals("")){
        	respBuilder.header("Access-Control-Allow-Headers", reqHead);
        }
 
        resp.setResponse(respBuilder.build());
        return resp;
	}

}
