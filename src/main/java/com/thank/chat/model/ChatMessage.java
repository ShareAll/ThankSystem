package com.thank.chat.model;

import com.google.gson.Gson;

public class ChatMessage {
	public String userName;
	public String textContent;
	
	public ChatMessage() {}
	public static ChatMessage fromJson(String rawMessage) {
		Gson gson=new Gson();
		return gson.fromJson(rawMessage, ChatMessage.class);
	}
	public ChatMessage(String userName,String textContent) {
		this.userName=userName;
		this.textContent=textContent;
	}
	public String toJson() {
		Gson gson=new Gson();
		return gson.toJson(this);
		
	}
}
