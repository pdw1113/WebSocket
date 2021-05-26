package nyu.socket.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController { 
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@GetMapping(value = "/")
	public String home(HttpSession session) {
		return "home";
	}
	
	@PostMapping(value = "/login")
	public ModelAndView login(HttpSession session, String name) {
		
		ModelAndView mav = null; 
		
		if(!"".equals(name) && name != null) {
			session.setAttribute("loginUser", name);
			mav = new ModelAndView("sample");
		}
		
		return mav;
	}
	
	@PostMapping(value = "/logout")
	public ModelAndView logout(HttpSession session) {
		
		ModelAndView mav = null;
		 
		mav = new ModelAndView("sample"); 
		
		// 로그인 세션
		String user = (String)session.getAttribute("loginUser");
		
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
		
		String name = (String)session.getAttribute("loginUser");
		String sender = map.get("sender");

		if(!name.equals(sender)) {
			mav.addObject("sender", sender);
		}
		
		mav.addObject("content", map.get("content"));
		mav.addObject("date", sdf.format(new Date()));
		return mav;
	}	
}
