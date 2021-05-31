package nyu.socket.test.user;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController { 
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userSerivce;
	
	@GetMapping(value = "/")
	public String home(HttpSession session) {
		return "home";
	}
	
	@PostMapping(value = "/login")
	public ModelAndView login(HttpSession session, String name) {
		
		// 유저 생성
		UserDTO user = userSerivce.selectUser("name");
		
		logger.debug(user.toString()); 
		
		// HTML
		ModelAndView mav = null; 
		
		// DB 존재 시
		if(user != null) {
			session.setAttribute("loginUser", user);
			mav = new ModelAndView("sample");
		}
		
		return mav;
	}
	
	@PostMapping(value = "/logout")
	public ModelAndView logout(HttpSession session) {
		
		ModelAndView mav = null;
		 
		mav = new ModelAndView("sample"); 
		
		// 로그인 세션
//		String user = (User)session.getAttribute("loginUser");
		
		session.invalidate();
		
		return mav;
	}	

	// JSON 타입의 파라미터를 받기 위해서는 @RequestBody 어노테이션을 붙여줘야 한다.
	@PostMapping(value = "/message")
	public ModelAndView message(HttpSession session, @RequestBody HashMap<String, String> map) { 
		
		logger.debug(map.toString());
		
		ModelAndView mav = null;
		
		mav = new ModelAndView("message"); 
		
		// Locale을 통해 한글 → 영어 표시 가능
		SimpleDateFormat sdf = new SimpleDateFormat("h:mm a | MMM d", new Locale("en", "US"));
		
		UserDTO user = (UserDTO)session.getAttribute("loginUser");
		String sender = map.get("sender");

		if(!user.getUserName().equals(sender)) {
			mav.addObject("sender", sender);
		}
		
		mav.addObject("content", map.get("content"));
		mav.addObject("date", sdf.format(new Date()));
		return mav;
	}	
	
	// JSON 타입의 파라미터를 받기 위해서는 @RequestBody 어노테이션을 붙여줘야 한다.
	@PostMapping(value = "/room")
	public ModelAndView room(HttpSession session, @RequestBody HashMap<String, String> map) {
		
		ModelAndView mav = null; 
		
		mav = new ModelAndView("room"); 
		
		// Locale을 통해 한글 → 영어 표시 가능
		SimpleDateFormat sdf = new SimpleDateFormat("h:mm a | MMM d", new Locale("en", "US"));
		// 날짜 추가         
		mav.addObject("date", sdf.format(new Date()));
		
		if(map.get("handle").equals("roomList")) {     
			map.remove("handle");
			mav.addObject("map", map);
			return mav;
		}

		// uuid 추가
		mav.addObject("uuid", map.get("uuid"));
		
		String sender = map.get("sender");
		
		mav.addObject("sender", sender);
		return mav;
	}	
}
