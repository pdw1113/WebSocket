package nyu.socket.test.socket;

import java.util.Arrays;
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
	
	private static Map<String, WebSocketSession> sessionMap = new HashMap<String, WebSocketSession>();
	private Map<String, WebSocketSession> chatMap = new HashMap<String, WebSocketSession>();
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		
		logger.debug("아이디 : " + session.getAttributes().get("name") + "웹소켓 세션 : " + session.toString() + " 연결 성공");
		
		sessionMap.put((String)session.getAttributes().get("name"), session);
		
		String[] strArr = new String[chatMap.size()];
		
		int i = 0;
		
		for(String chatRoom : chatMap.keySet()) {
			strArr[i++] = chatRoom;
		}

		WebSocketMessage<?> message = new TextMessage(Arrays.toString(strArr));
		
		for ( String key : sessionMap.keySet()) {
			sessionMap.get(key).sendMessage(message);
		}
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		
		JSONParser jParser = new JSONParser();
		JSONObject jObject = (JSONObject) jParser.parse((String)message.getPayload());
				
		if(jObject.get("handle").toString().equals("send")) {
			
			logger.debug(jObject.get("sender").toString());
			logger.debug(jObject.get("content").toString()); 
			logger.debug(jObject.get("c").toString());
			
			sessionMap.put(jObject.get("sender").toString(), session);
			
			message = new TextMessage("send," + jObject.get("sender").toString() + "," + jObject.get("content").toString() + "," + jObject.get("c").toString());
			
			logger.debug("세션 :  "	+ session.toString());
			logger.debug("메세지 : "	+ message.getPayload());
			logger.debug("메세지 : "	+ message.toString());
			
			logger.debug(sessionMap.toString());
			
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
		
		sessionMap.remove(session.getAttributes().get("name"));
		logger.debug(session.getAttributes().get("name") + "님의 웹소켓 연결 해제");
	}

	@Override
	public boolean supportsPartialMessages() {
		
		logger.debug("supportsPartialMessages");
		return false;
	}

}
