package com.thank.jersey.plugin;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WFCorsFilter implements Filter {
	public WFCorsFilter() {
		
	}
		
	public void init(FilterConfig fConfig) throws ServletException {
		
	}

	public void destroy() {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain) throws IOException, ServletException {
		HttpServletResponse resp=(HttpServletResponse)response;
		resp.addHeader("Access-Control-Allow-Origin", "*");
		resp.addHeader("Access-Control-Allow-Methods", "GET,PUT,POST, OPTIONS");
		HttpServletRequest req=(HttpServletRequest)request;
		String reqHead = req.getHeader("Access-Control-Request-Headers");

		if(null != reqHead && !reqHead.equals("")){
			resp.addHeader("Access-Control-Allow-Headers", reqHead);
			
		}
		chain.doFilter(request, response);
	}

}
