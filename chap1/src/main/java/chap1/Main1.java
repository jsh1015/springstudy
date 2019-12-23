package chap1;

import org.springframework.context.support.GenericXmlApplicationContext;

public class Main1 {
	public static void main(String[] args) {
		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext("classpath:applicationContext.xml");
		Greeter g = ctx.getBean("greeter",Greeter.class); //greeter 이름을 가진 Greeter객체를 g로 만듬
		System.out.println(g.greet("스프링"));
		
		
		Message m = ctx.getBean("message2",Message.class); //Message인터페이스로 구현했기때문에 가져올수있음
		m.sayHello("Hongkildong");
		m = ctx.getBean("message",Message.class);
		m.sayHello("홍길동");
		
		Greeter g2 = ctx.getBean("greeter",Greeter.class);
		if(g==g2) {
			System.out.println("g객체와 g2객체는 같은 객체다.");
		}
	}
}