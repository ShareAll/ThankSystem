package com.thank.socket;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.websocket.Endpoint;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;

public class MainConfig implements ServerApplicationConfig {
	public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> scanned) {
		return scanned;
	}

	@Override
	public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> endpointClasses) {
		Set<ServerEndpointConfig> result = new HashSet<>();
		return result;
	}
	
	public static class GetHttpSessionConfigurator extends ServerEndpointConfig.Configurator
	{
	    @Override
	    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response)
	    {
	        HttpSession httpSession = (HttpSession)request.getHttpSession();
	        config.getUserProperties().put(HttpSession.class.getName(),httpSession);
	    }
	}
}
