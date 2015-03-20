package com.thank.rest.shared.model;
/***
 * Redirect model for rest service
 * @author fenwang
 *
 */
public class WFRestRedirectModel {
	public String location;
	public String lastError;
	public WFRestRedirectModel() {
		
	}
	public WFRestRedirectModel(String location,String lastError) {
		this.location=location;
		this.lastError=lastError;
	}
}
