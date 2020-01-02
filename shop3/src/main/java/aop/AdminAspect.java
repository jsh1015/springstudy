package aop;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import exception.CartEmptyException;
import exception.LoginException;
import logic.Cart;
import logic.User;

@Component
@Aspect
public class AdminAspect {
	@Around("execution(* controller.Admin*.*(..))")
	public Object admin(ProceedingJoinPoint joinPoint) throws Throwable{
		User loginUser = null;
		for(Object o :joinPoint.getArgs()) { 
			//매개변수 HttpSession가 있는 부분이 상관없게끔 for문을 돌림
			if(o instanceof HttpSession) {
				HttpSession session = (HttpSession)o;
				loginUser = (User)session.getAttribute("loginUser");
			}
		}
		if(loginUser == null) {
			throw new LoginException("로그인 후 거래하세요","../user/login.shop");
		}
		if(!loginUser.getUserid().equals("admin")) {
			throw new LoginException("관리자만 가능한 거래입니다.","../user/main.shop?id="+loginUser.getUserid());
		}
		Object ret = joinPoint.proceed();
		return ret;
		
//		HttpSession session = (HttpSession)joinPoint.getArgs()[0]; //매개변수 첫번째에 session이 있어야함
//		User loginUser = (User)session.getAttribute("loginUser");
	}
}
