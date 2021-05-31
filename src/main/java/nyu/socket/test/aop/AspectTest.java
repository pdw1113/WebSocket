package nyu.socket.test.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AspectTest{
              
	@After("execution(* nyu.socket.test.user.UserController.login(..))")
	public void afterLogin(JoinPoint joinPoint) {
		
		System.out.println("this is aspect!!");
		
		Object[] signatureArgs = joinPoint.getArgs();           
		   for (Object signatureArg: signatureArgs) {         
			      System.out.println("Arg: " + signatureArg);      
		}
		
	}
	
}
 