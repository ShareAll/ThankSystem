package com.thank.servlet;

import java.io.IOException;
import java.io.Serializable;
import java.util.Timer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thank.tasks.SendMailTask;

public class JikarmaServlet  extends HttpServlet {

	private static final long serialVersionUID = 3126101123057122328L;
	private Timer  sendEmailTimer;
	
	public JikarmaServlet(){
		super();
	}

	@Override
	public void init() {
		sendEmailTimer = new Timer(true);
		sendEmailTimer.scheduleAtFixedRate(new SendMailTask() , 0, 10*1000);
		
	}
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		super.doGet(request, response);
	}
	
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		super.doPost(request, response);
	}
	
	@Override
	public void destroy() {
		if (sendEmailTimer != null) {
			sendEmailTimer.cancel();
		}
	}
	
}
