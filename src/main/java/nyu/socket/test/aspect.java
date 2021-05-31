package nyu.socket.test;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class aspect{
              
	@After("execution(* nyu.socket.test.HomeController.login(..))")
	public void afterLogin(JoinPoint joinPoint) {
		
		System.out.println("this is aspect!!");
		
		Object[] signatureArgs = joinPoint.getArgs();           
		   for (Object signatureArg: signatureArgs) {         
			      System.out.println("Arg: " + signatureArg);      
		}
		
	}
	
}
 