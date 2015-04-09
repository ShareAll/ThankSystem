package com.thank.socket;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import com.thank.chat.model.ChatMessage;

public class ChatRoom {
	private static ConcurrentHashMap<String, ChatRoom> rooms=new ConcurrentHashMap<String,ChatRoom>();
	public static ChatRoom lookup(String roomId) {
		ChatRoom room=rooms.get(roomId);
		if(room==null) {
			room=new ChatRoom(roomId);
			rooms.put(room.roomId, room);
		}
		return room;
	}
	
	private String roomId;
	private Set<ChatSession> sessions=new CopyOnWriteArraySet<>();
	public ChatRoom(String roomId) {
		this.roomId=roomId;
	}
	
	
	public void addSession(ChatSession session) {
		sessions.add(session);
	}
	public void removeSession(ChatSession session) {
		sessions.remove(session);		
		if(sessions.size()==0) rooms.remove(this.roomId);
	}
	
	public void sendMsg(ChatMessage msg) {
		String jsonMsg=msg.toJson();
		for (ChatSession client : sessions) {
			if(!client.sendMsg(jsonMsg)) sessions.remove(client);
		}
	}
	
}
