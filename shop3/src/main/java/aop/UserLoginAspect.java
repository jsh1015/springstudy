package aop;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import exception.LoginException;
import logic.User;

@Component
@Aspect
@Order(1)
public class UserLoginAspect {
	@Around //advice : 핵심로직 전,후
		("execution(* controller.User*.check*(..)) && args(.., session)")
		//pointcut : 핵심로직 설정(접근제어자 상관X controller패키지.User로 시작하는 모든클래스.check로시작하는 모든메서드
		//&& 매개변수가 앞에는 상관없고 뒤에있는 매개변수는 session으로 끝나야함)
	public Object userLoginCheck(ProceedingJoinPoint joinPoint, HttpSession session)throws Throwable{
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser == null) {
			throw new LoginException("로그인 후 거래하세요","login.shop");
		}
		Object ret = joinPoint.proceed();
		return ret;
	}
	
	@Around //advice : 핵심로직 전,후
	("execution(* controller.User*.check*(..)) && args(id, session)")
	public Object userIdCheck(ProceedingJoinPoint joinPoint, String id ,HttpSession session)throws Throwable{
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser == null) {
			throw new LoginException("2. 로그인 후 거래하세요","login.shop");
		}
		if(!loginUser.getUserid().equals("admin") && !loginUser.getUserid().equals(id)) {
			throw new LoginException("본인의 계정만 조회가 가능합니다.","main.shop");
		}
		Object ret = joinPoint.proceed();
		return ret;
	}
}