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

import nyu.socket.test.user.UserDTO;


public class WebSocketHandler implements org.springframework.web.socket.WebSocketHandler {

	private static final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);
	
	// 로그인 유저 목록 : <User, WebSocketSession>
	private Map<UserDTO, WebSocketSession> sessionMap = new HashMap<UserDTO, WebSocketSession>();
	// 채팅방 목록 : <String, WebSocketSession>
	private Map<String, UserDTO> userMap = new HashMap<String, UserDTO>();
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		
		UserDTO user = (UserDTO)session.getAttributes().get("loginUser");
		
		
		logger.debug("\n아이디 : " + session.getAttributes().get("loginUser") + 
					 "\n웹소켓 세션 : " + session.getId() + 
					 "\n연결 성공");
		
		// 유저 - 세션 목록에 추가
		sessionMap.put(user, session);
		// uuid - 유저 목록에 추가
		userMap.put(user.getUserUuid(), user);
		 
		logger.debug("로그인 유저 목록 : " + sessionMap.toString());
	}

	@Override  
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		// 메세지(JSON) 파싱
		JSONParser jParser = new JSONParser();
		JSONObject jObject = (JSONObject) jParser.parse((String)message.getPayload());
		
		// 보낸 사람 (나)
		UserDTO sender = (UserDTO)session.getAttributes().get("loginUser");
		String senderName = sender.getUserName();
		String senderUuid = sender.getUserUuid();
				
		if(jObject.get("handle").toString().equals("message")) {
			
			// 채팅 메세지
			message = new TextMessage("message" + "," + jObject.get("sender").toString() + "," + jObject.get("content").toString() + "," + sender.getUserUuid());
			// UUID로 user 찾기
			UserDTO receiver = userMap.get(jObject.get("uuid"));
			// 메세지 전송
			sessionMap.get(receiver).sendMessage(message);
			
			
		}else if(jObject.get("handle").toString().equals("login")) {
			
			// 채팅방 생성 메세지 (로그인 키워드)
			message = new TextMessage("login" + "," + senderName + "," + senderUuid);
			// 채팅방 목록 키워드
			String loginedUsers = "roomList";
			
			// 1. 로그인 되어 있는 유저들의 채팅방 화면에 전송하여 로그인 알림
			for (UserDTO users : sessionMap.keySet()) {
				// 자기 자신은 제외
				if(users.getUserUuid().equals(senderUuid)) continue;
				// 유저들에게 로그인 했다는 메세지를 보내면 프론트에서 채팅방으로 만들어준다.
				sessionMap.get(users).sendMessage(message); 
				// 로그인 된 유저들 (,) 구분자로 더하기
				loginedUsers += "," + users.getUserName() + ":" + users.getUserUuid();
			}
			// 채팅방 호출 메세지
			message = new TextMessage(loginedUsers);
			// 2. 자기 자신에게 로그인된 유저들의 채팅방 목록을 만들어주라는 메세지를 보낸다.
			sessionMap.get(sender).sendMessage(message); 
			
		}else if(jObject.get("handle").toString().equals("logout")) {
			
			// 채팅방 삭제 메세지 (로그아웃 키워드)
			message = new TextMessage("logout" + "," + senderUuid);                 
			                      
			// 로그인 되어 있는 유저들의 채팅방 화면에 전송하여 로그아웃 알림
			for (UserDTO users : sessionMap.keySet()) {
				// 자기 자신은 제외
				if(users.getUserName().equals(senderName)) continue;
				// 유저들에게 로그인 했다는 메세지를 보내면 프론트에서 채팅방으로 만들어준다.
				sessionMap.get(users).sendMessage(message);
			}
			
		}else if(jObject.get("handle").toString().equals("roomList")) {
			
			// 채팅방 목록 키워드
			String loginedUsers = "roomList";
			// 로그인 되어 있는 유저 불러오기
			for (UserDTO users : sessionMap.keySet()) {
				// 자기 자신은 제외
				if(users.getUserName().equals(senderName)) continue;
				// 로그인 된 유저들 (,) 구분자로 더하기         
				loginedUsers += "," + users.getUserName() + ":" + users.getUserUuid();
			}
			// 채팅방 호출 메세지
			message = new TextMessage(loginedUsers);
			// 2. 자기 자신에게 로그인된 유저들의 채팅방 목록을 만들어주라는 메세지를 보낸다.
			sessionMap.get(sender).sendMessage(message); 
		}
	}
	
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		
		logger.debug("handleTransportError");
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		
		UserDTO u = (UserDTO)session.getAttributes().get("loginUser");
		
		sessionMap.remove(u);
		userMap.remove(u.getUserUuid());
		
		logger.debug((UserDTO)session.getAttributes().get("loginUser") + "님의 웹소켓 연결 해제");
	}

	@Override
	public boolean supportsPartialMessages() {
		
		logger.debug("supportsPartialMessages");
		return false;
	}

}
