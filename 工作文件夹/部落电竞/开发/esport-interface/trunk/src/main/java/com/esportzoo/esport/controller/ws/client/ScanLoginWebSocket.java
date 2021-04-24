package com.esportzoo.esport.controller.ws.client;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ServerEndpoint("/websocket/{sceneStr}")
public class ScanLoginWebSocket {
	
	private static final Logger logger = LoggerFactory.getLogger(ScanLoginWebSocket.class);
	
	private static int onlineCount = 0;
	private static Map<String, ScanLoginWebSocket> clients = new ConcurrentHashMap<String, ScanLoginWebSocket>();
	private Session session;
	private String sceneStr;

	
	
	@OnOpen
	public void onOpen(@PathParam("sceneStr") String sceneStr, Session session) throws IOException {
		logger.info("sceneStr:"+sceneStr);
		
		this.sceneStr = sceneStr;
		this.session = session;

		addOnlineCount();
		clients.put(sceneStr, this);
		logger.info("===================已连接==================");
	}

	@OnClose
	public void onClose() throws IOException {
		clients.remove(sceneStr);
		subOnlineCount();
		logger.info("===================已断开连接==================");
	}

	@OnMessage
	public void onMessage(String message) throws IOException {

//		JSONObject jsonTo = JSONObject.fromObject(message);
//		String mes = (String) jsonTo.get("message");
		logger.info("接收到客户端的消息【message:】"+message);
//		if (!jsonTo.get("To").equals("All")) {
//			logger.info("message===================:"+mes);
//			sendMessageTo(mes, jsonTo.get("To").toString());
//		} else {
//			logger.info("message===================:"+mes);
//			sendMessageAll("给所有人");
//		}
	}

	@OnError
	public void onError(Session session, Throwable error) {
		error.printStackTrace();
	}

	public void sendMessageTo(String message, String To) throws IOException {
		// session.getBasicRemote().sendText(message);
		// session.getAsyncRemote().sendText(message);
		for (ScanLoginWebSocket item : clients.values()) {
			if (item.sceneStr.equals(To))
				logger.info("服务器推送的消息【message:】"+message);
				item.session.getAsyncRemote().sendText(message);
		}
	}

	public void sendMessageAll(String message) throws IOException {
		
		for (ScanLoginWebSocket item : clients.values()) {
			
			item.session.getAsyncRemote().sendText(message);
		}
	}

	public static synchronized int getOnlineCount() {
		return onlineCount;
	}

	public static synchronized void addOnlineCount() {
		ScanLoginWebSocket.onlineCount++;
	}

	public static synchronized void subOnlineCount() {
		ScanLoginWebSocket.onlineCount--;
	}

	public static synchronized Map<String, ScanLoginWebSocket> getClients() {
		return clients;
	}
	
}
