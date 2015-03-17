package com.thank.jersey.plugin;

import java.io.IOException;
import java.util.HashSet;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Strings;
import com.thank.rest.resources.UserContextUtil;
import com.thank.user.model.UserInfo;

public class AuthFilter implements Filter {
	
	private String contextPath;
	private String loginPage;
	private HashSet<String> excludedUrls=new HashSet<String>();

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig fc) throws ServletException {
		this.loginPage=fc.getInitParameter("loginPage");
		String pathsString=fc.getInitParameter("excludedUrls");
		if(Strings.isNullOrEmpty(loginPage) || Strings.isNullOrEmpty(pathsString)) {
			throw new Error("the init params of loginPath and excludedUrls must be set");
		}
		contextPath = fc.getServletContext().getContextPath();
		String[] paths=pathsString.split(",");
		for(String path:paths) {
			this.excludedUrls.add(contextPath+"/"+path.trim());
		}
		this.excludedUrls.add(contextPath+"/"+loginPage);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,FilterChain fc) throws IOException, ServletException {		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;  
		UserInfo user=UserContextUtil.getCurUser(req);
		if(user==null) {
			boolean excluded=false;
			for(String excludeUrl:excludedUrls) {
				if(req.getRequestURI().startsWith(excludeUrl)) {
					excluded=true;
					break;
				}
			}
			if(!excluded) {
				res.sendRedirect(contextPath + loginPage);
			} else {
				fc.doFilter(request, response);
			}
		} else {
			fc.doFilter(request, response);
		}
	}
}