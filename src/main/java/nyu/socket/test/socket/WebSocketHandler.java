package nyu.socket.test.socket;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

public class WebSocketHandler implements org.springframework.web.socket.WebSocketHandler {

	private static final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);
	
	// 로그인 유저 목록 : <이름, WebSocketSession>
	private Map<String, WebSocketSession> sessionMap = new HashMap<String, WebSocketSession>();
	// 채팅방 목록 : <방이름, WebSocketSession>
	private Map<String, WebSocketSession> chatMap = new HashMap<String, WebSocketSession>();
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		
		logger.debug("\n아이디 : " + session.getAttributes().get("loginUser") + 
					 "\n웹소켓 세션 : " + session.getId() + 
					 "\n연결 성공");
		
		// 로그인 유저 목록에 추가
		sessionMap.put((String)session.getAttributes().get("loginUser"), session);
		 
		logger.debug(sessionMap.toString());
		
		/*
		 * String[] strArr = new String[chatMap.size()];
		 * 
		 * int i = 0;
		 * 
		 * for(String chatRoom : chatMap.keySet()) { strArr[i++] = chatRoom; }
		 * 
		 * WebSocketMessage<?> message = new TextMessage(Arrays.toString(strArr));
		 * 
		 * for ( String key : sessionMap.keySet()) {
		 * sessionMap.get(key).sendMessage(message); }
		 */
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
			
			for ( String key : sessionMap.keySet()) {
				sessionMap.get(key).sendMessage(message);
			}
			  
		}else if(jObject.get("handle").toString().equals("create")) {
			chatMap.put(jObject.get("room").toString(), session);
			logger.debug(jObject.get("room").toString() + "채팅방 생성!");
			
			message = new TextMessage("room," + jObject.get("room").toString());
			
			logger.debug(sessionMap.toString());
			
			for ( String key : sessionMap.keySet()) {
				sessionMap.get(key).sendMessage(message);
			}
		}
	}
	
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		
		logger.debug("handleTransportError");
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		
		sessionMap.remove(session.getAttributes().get("loginUser"));
		logger.debug(session.getAttributes().get("loginUser") + "님의 웹소켓 연결 해제");
	}

	@Override
	public boolean supportsPartialMessages() {
		
		logger.debug("supportsPartialMessages");
		return false;
	}

}
