package main;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration	//설정 java 소스. xml 설정 대체하는 소스
@ComponentScan(basePackages = {"annotation,main"}) //annotation.xml의 component-scan
											//<context:component-scan base-package="annotation,main" />
@EnableAspectJAutoProxy	//AOP설정 : <aop:aspectj-autoproxy /> :aop를 사용할것이라는 설정
public class AppCtx {
	/*
	@Bean	//<bean id="memberService2" class="main.MemberService"> : 컨테이너에 등록
			//									리턴값
	public MemberService memberService2() {
		return new MemberService();
	}
	*/
}