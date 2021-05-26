package nyu.socket.test.socket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import nyu.socket.test.user.User;

public class WebSocketHandler implements org.springframework.web.socket.WebSocketHandler {

	private static final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);
	
	// 로그인 유저 목록 : <User, WebSocketSession>
	private Map<User, WebSocketSession> sessionMap = new HashMap<User, WebSocketSession>();
	// 채팅방 목록 : <String, WebSocketSession>
	private Map<String, ArrayList<WebSocketSession>> chatMap = new HashMap<String, ArrayList<WebSocketSession>>();
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		
		User user = (User)session.getAttributes().get("loginUser");
		
		
		logger.debug("\n아이디 : " + session.getAttributes().get("loginUser") + 
					 "\n웹소켓 세션 : " + session.getId() + 
					 "\n연결 성공");
		
		// 로그인 유저 목록에 추가
		sessionMap.put(user, session);
		 
		logger.debug("로그인 유저 목록 : " + sessionMap.toString());
		 
		WebSocketMessage<?> message = new TextMessage("login," + user.getName());
		
		// 채팅방 화면에 전송
		for (User users : sessionMap.keySet()) {
			sessionMap.get(users).sendMessage(message); 
		}
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		
		JSONParser jParser = new JSONParser();
		JSONObject jObject = (JSONObject) jParser.parse((String)message.getPayload());
				
		if(jObject.get("handle").toString().equals("message")) {
			
			message = new TextMessage("message," + jObject.get("sender").toString() + "," + jObject.get("content").toString());
			
			logger.debug("세션 :  "	+ session.toString());
			logger.debug("메세지 : "	+ message.toString());
			
			logger.debug("메세지 받을 세션들 : " + sessionMap.toString());
			
			for (User user : sessionMap.keySet()) {
				sessionMap.get(user).sendMessage(message);
			}
		}
	}
	
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		
		logger.debug("handleTransportError");
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		
		sessionMap.remove((User)session.getAttributes().get("loginUser"));
		logger.debug((User)session.getAttributes().get("loginUser") + "님의 웹소켓 연결 해제");
	}

	@Override
	public boolean supportsPartialMessages() {
		
		logger.debug("supportsPartialMessages");
		return false;
	}

}
