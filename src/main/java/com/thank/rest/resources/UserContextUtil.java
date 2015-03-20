package com.thank.rest.resources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.thank.common.dao.UserDao;
import com.thank.common.model.UserInfo;
import com.thank.jersey.plugin.GsonUtil;
import com.thank.rest.shared.model.WFRestException;
/**
 * Context util to wrap implementation of context mgmt
 * @author fenwang
 *
 */
public class UserContextUtil {
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
	public static void saveInSession(HttpServletRequest request,UserInfo user) {
		HttpSession session=request.getSession();
		session.setAttribute(SESSION_CUR_USER, user);
	}
	public static void logout(HttpServletRequest request) {
		HttpSession session=request.getSession();
		session.invalidate();
	}
		
}
