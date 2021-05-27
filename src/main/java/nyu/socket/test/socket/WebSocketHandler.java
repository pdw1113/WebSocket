package nyu.socket.test.socket;

import java.util.ArrayList;
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
	}

	@Override  
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		// 메세지(Json) 파싱
		JSONParser jParser = new JSONParser();
		JSONObject jObject = (JSONObject) jParser.parse((String)message.getPayload());
				
		if(jObject.get("handle").toString().equals("message")) {
			
			// 채팅 메세지
			message = new TextMessage("message" + "," + jObject.get("sender").toString() + "," + jObject.get("content").toString());
			
			logger.debug("세션 :  "	+ session.toString());
			logger.debug("메세지 : "	+ message.toString()); 
			 
			logger.debug("메세지 받을 세션들 : " + sessionMap.toString());
			
			for (User user : sessionMap.keySet()) {
				sessionMap.get(user).sendMessage(message);
			}
			
		}else if(jObject.get("handle").toString().equals("login")) {
			
			// 로그인 한 유저 (자기 자신)
			User user = (User)session.getAttributes().get("loginUser");
			// 채팅방 생성 메세지 (로그인 키워드)
			message = new TextMessage("login" + "," + user.getName() + "," + user.getUuid());
			// 채팅방 목록 키워드
			String loginedUsers = "roomList";
			
			// 1. 로그인 되어 있는 유저들의 채팅방 화면에 전송하여 로그인 알림
			for (User users : sessionMap.keySet()) {
				// 자기 자신은 제외
				if(users.getUuid().equals(user.getUuid())) continue;
				// 유저들에게 로그인 했다는 메세지를 보내면 프론트에서 채팅방으로 만들어준다.
				sessionMap.get(users).sendMessage(message); 
				// 로그인 된 유저들 (,) 구분자로 더하기
				loginedUsers += "," + users.getName() + ":" + users.getUuid();
			}
			// 채팅방 호출 메세지
			message = new TextMessage(loginedUsers);
			// 2. 자기 자신에게 로그인된 유저들의 채팅방 목록을 만들어주라는 메세지를 보낸다.
			sessionMap.get(user).sendMessage(message); 
			
		}else if(jObject.get("handle").toString().equals("logout")) {
			
			// 로그아웃 한 유저 (자기 자신)
			User user = (User)session.getAttributes().get("loginUser");
			// 채팅방 삭제 메세지 (로그아웃 키워드)
			message = new TextMessage("logout" + "," + user.getUuid());                 
			                      
			// 로그인 되어 있는 유저들의 채팅방 화면에 전송하여 로그아웃 알림
			for (User users : sessionMap.keySet()) {
				// 자기 자신은 제외
				if(users.getName().equals(user.getName())) continue;
				// 유저들에게 로그인 했다는 메세지를 보내면 프론트에서 채팅방으로 만들어준다.
				sessionMap.get(users).sendMessage(message);
			}
			
		}else if(jObject.get("handle").toString().equals("roomList")) {       
			
			// 자기 자신
			User user = (User)session.getAttributes().get("loginUser");
			// 채팅방 목록 키워드
			String loginedUsers = "roomList";
			// 로그인 되어 있는 유저 불러오기
			for (User users : sessionMap.keySet()) {
				// 자기 자신은 제외
				if(users.getName().equals(user.getName())) continue;
				// 로그인 된 유저들 (,) 구분자로 더하기         
				loginedUsers += "," + users.getName() + ":" + users.getUuid();
			}
			// 채팅방 호출 메세지
			message = new TextMessage(loginedUsers);
			// 2. 자기 자신에게 로그인된 유저들의 채팅방 목록을 만들어주라는 메세지를 보낸다.
			sessionMap.get(user).sendMessage(message); 
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
