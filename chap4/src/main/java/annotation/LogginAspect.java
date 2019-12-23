package annotation;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect	//aspect 클래스 설정(xml에 설정하지않음)
@Order(3)//세번째로 사용될 aop클래스라는 순서를 지정
public class LogginAspect {
	final String publicMethod = "execution(public * annotation..*(..))"; //pointcut으로 사용될것을 미리 지정
	@Before(publicMethod)
	public void before() {
		System.out.println("[LA] 메서드 실행 전 실행 (1)");
	}
	@AfterReturning(pointcut=publicMethod,returning="ret")
	public void afterReturning(Object ret) {
		System.out.println("[LA] 메서드 정상 종료 후 실행. (2) 리턴값="+ret);
	}
	@AfterThrowing(pointcut=publicMethod,throwing="ex")
	public void afterThrowing(Throwable ex) {
		System.out.println("[LA] 메서드 예외 종료 후 실행. (3) 예외 메세지="+ex.getMessage());
	}
	@After(publicMethod)
	public void afterFinally() {
		System.out.println("[LA] 메서드 종료 후 실행 (4)");
	}
}
