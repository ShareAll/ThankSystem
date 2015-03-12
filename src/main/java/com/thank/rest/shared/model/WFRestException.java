package com.thank.rest.shared.model;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class WFRestException extends WebApplicationException {
	private static final long serialVersionUID = -8270279937591832474L;
	private int respCode;
	private String errorMsg;
	public static class ErrorStatus implements Serializable{
		private static final long serialVersionUID = 7513513406292992433L;
		public String status="Failure";
		public String errorMsg;
		public String stackTrace;
		public ErrorStatus() {}
		public ErrorStatus(String errorMsg,Exception e) {
			this.errorMsg=errorMsg;
			StringWriter sw=new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			this.stackTrace=sw.toString();
		}
	}
	public WFRestException(int respCode,String errorMsg) {
		this.respCode=respCode;
		this.errorMsg=errorMsg;
	}
	@Override
	public Response getResponse() {
		return Response.status(respCode).entity(new ErrorStatus(this.errorMsg,this)).build();
	}
	
	 
}
