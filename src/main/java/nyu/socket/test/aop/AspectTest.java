package nyu.socket.test.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AspectTest{
              
	/**
	 * 로그인 후
	 * @param joinPoint
	 */
	@After("execution(* nyu.socket.test.user.UserController.login(..))")
	public void afterLogin(JoinPoint joinPoint) {
		
		/*Object[] signatureArgs = joinPoint.getArgs();           
		   for (Object signatureArg: signatureArgs) {         
			      System.out.println("Arg: " + signatureArg);      
		}*/
		
	}
	
	@After("execution(* nyu.socket.test.socket.WebSocketHandler.handleMessage(..))")
	public void afterMessage(JoinPoint joinPoint) {
		System.out.println("채팅 AOP");
		
		Object[] signatureArgs = joinPoint.getArgs();           
		for (Object signatureArg: signatureArgs) {         
			System.out.println("Arg: " + signatureArg);      
			// handle = "message"일 때 DB 삽입
		}
		
	}
}
 