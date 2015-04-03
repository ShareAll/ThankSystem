package com.thank.socket;

import java.util.HashSet;
import java.util.Set;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;

public class MainConfig implements ServerApplicationConfig {
	public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> scanned) {
	        // Deploy all WebSocket endpoints defined by annotations in the examples
	        // web application. Filter out all others to avoid issues when running
	        // tests on Gump
	        Set<Class<?>> results = new HashSet<>();
	        for (Class<?> clazz : scanned) {
	            if (clazz.getPackage().getName().startsWith("com.thank.socket.")) {
	                results.add(clazz);
	            }
	        }
	        return results;
	    }

	@Override
	public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> endpointClasses) {
		Set<ServerEndpointConfig> result = new HashSet<>();
		return result;
	}
}
