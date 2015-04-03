package com.thank.socket;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/ws/chat")
public class ChatSession {
	private static final AtomicInteger connectionIds = new AtomicInteger(0);
	private static final Set<ChatSession> connections =new CopyOnWriteArraySet<>();
	
	private final String nickname;
	private Session session;
	public ChatSession() {
		nickname = "guest" + connectionIds.getAndIncrement();
	}
	
	@OnOpen
	public void start(Session session) {
		this.session = session;
		connections.add(this);
		String message = String.format("* %s %s", nickname, "has joined.");
		broadcast(message);
	}
	@OnClose
	public void end() {
		connections.remove(this);
		String message = String.format("* %s %s",nickname, "has disconnected.");
		broadcast(message);
	}

	@OnMessage
	public void incoming(String message) {
		// Never trust the client
		String filteredMessage = String.format("%s: %s",nickname, filter(message.toString()));
		broadcast(filteredMessage);
	}
	 
	@OnError
	public void onError(Throwable t) throws Throwable {
	//	log.error("Chat Error: " + t.toString(), t);
	}
	
	public static String filter(String message) {
		if (message == null) return null;
		char content[] = new char[message.length()];
		message.getChars(0, message.length(), content, 0);
		StringBuilder result = new StringBuilder(content.length + 50);
		for (int i = 0; i < content.length; i++) {
			switch (content[i]) {
				case '<':
					result.append("&lt;");
					break;
				case '>':
					result.append("&gt;");
					break;
				case '&':
					result.append("&amp;");
					break;
				case '"':
					result.append("&quot;");
					break;
				default:
					result.append(content[i]);
			}
		 }
		return (result.toString());
	}
	
	private static void broadcast(String msg) {
		for (ChatSession client : connections) {
			try {
				synchronized (client) {
					client.session.getBasicRemote().sendText(msg);
				}
			} catch (IOException e) {
				connections.remove(client);
				try {
					client.session.close();
				} catch (IOException e1) {
				}
				String message = String.format("* %s %s",client.nickname, "has been disconnected.");
				broadcast(message);
			}
		}
	 }	
}
