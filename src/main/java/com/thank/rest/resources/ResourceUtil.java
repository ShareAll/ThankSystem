package com.thank.rest.resources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.thank.rest.shared.model.WFRestException;
import com.thank.user.model.UserInfo;

public class ResourceUtil {
	private static final String SESSION_CUR_USER="CUR_USER";
	public static void authenticate(HttpServletRequest request) {
		HttpSession session=request.getSession();
		Object ret=session.getAttribute(SESSION_CUR_USER);
		if(ret==null || !(ret instanceof UserInfo) ) {
			throw new WFRestException(401,"Not login yet");
		} 
	}
	public static UserInfo getCurUser(HttpServletRequest request) {
		HttpSession session=request.getSession();
		Object ret=session.getAttribute(SESSION_CUR_USER);
		if(ret==null || !(ret instanceof UserInfo) ) {
			return null;
		} else {
			return (UserInfo) ret;
		}	
	}
	public static void login(HttpServletRequest request,UserInfo user) {
		HttpSession session=request.getSession();
		session.setAttribute(SESSION_CUR_USER, user);
	}
	public static void logout(HttpServletRequest request) {
		HttpSession session=request.getSession();
		session.invalidate();
	}
		
}
