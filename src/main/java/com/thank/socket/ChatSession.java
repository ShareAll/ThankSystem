package com.thank.socket;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;
import com.thank.chat.model.ChatMessage;
import com.thank.common.model.UserInfo;
import com.thank.rest.resources.UserContextUtil;

@ServerEndpoint(value = "/ws/chat/{topic}",configurator = GetHttpSessionConfigurator.class)
public class ChatSession {
	private static final AtomicInteger connectionIds = new AtomicInteger(0);
	private UserInfo curUser=null;
	private Session session=null;
	public ChatSession() {
	}
	public boolean sendMsg(String msg) {
		if(session==null) return false;
		try {
			synchronized(this) {
				session.getBasicRemote().sendText(msg);
			}
		}  catch (IOException e) {
			try {
				session.close();
				session=null;
			} catch (IOException e1) {
			}
			return false;
		}
		return true;
	}
	@OnOpen
	public void start(Session session,EndpointConfig config,@PathParam("topic") String topic) {
		this.session = session;
        HttpSession httpSession= (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        curUser=UserContextUtil.getCurUser(httpSession);
        if(curUser==null) {
        	curUser=new UserInfo();
        	curUser.setName("Guest "+connectionIds.getAndIncrement());
        } else if(curUser.getName()==null || curUser.getName().length()==0) {
        	curUser.setName(curUser.getEmailAddress());
        }
        
        ChatRoom room=room(topic);
		room.addSession(this);
		room.sendMsg(new ChatMessage(curUser.getName(),"I am joined"));
	}
	@OnClose
	public void end(@PathParam("topic") String topic) {
		room(topic).removeSession(this);
	}
	public ChatRoom room(String topic) {
		return ChatRoom.lookup(topic);
	}
	@OnMessage
	public void incoming(String raw,@PathParam("topic") String topic) {
		ChatMessage msg=ChatMessage.fromJson(raw);
		msg.userName=curUser.getName();
		room(topic).sendMsg(msg);
	}
	 
	@OnError
	public void onError(Throwable t) throws Throwable {
	//	log.error("Chat Error: " + t.toString(), t);
	}
	

		
}
