package main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import xml.Article;
import xml.Member;
import xml.MemberService;
import xml.ReadArticleService;
import xml.UpdateInfo;

public class Main2 {
	public static void main(String[] args) {
		String[] config = {"di.xml","aop2.xml"};
		//service : xml.ReadArticleServiceImpl 객체 저장
		ApplicationContext ctx = new ClassPathXmlApplicationContext(config);
		ReadArticleService service = ctx.getBean("readArticleService",ReadArticleService.class);
		try {
			Article a1 = service.getArticleAndReadCnt(1); //호출
			System.out.println(a1 + "\n");
			Article a2 = service.getArticleAndReadCnt(1); //같은값이기때문에 있는 것을 그대로 실행
			System.out.println("[main] a1==a2 : " + (a1==a2));
			System.out.println(a2 + "\n");
			service.getArticleAndReadCnt(0);
		}catch(Exception e) {
			System.out.println("[main]" + e.getMessage());
		}
		System.out.println("\n=====================================================\n *** UpdateMemberInfoTrace Aspect 연습 ***");
		MemberService msvc = ctx.getBean("memberService",MemberService.class);
		msvc.regist(new Member());
		System.out.println();
		
		msvc.update("hong", new UpdateInfo());
		System.out.println();
		
		msvc.delete("hong2", "test");
		System.out.println("========================================================\n *** main.MemberService 메서드 호출 ***");
		
		main.MemberService msvc2 = new main.MemberService(); //컨테이너에 있는 클래스가 아니기때문에 [TA]가 실행되지 않음
				//ctx.getBean("memberService2",main.MemberService.class);
		msvc2.regist(new Member());
		System.out.println();
		
		msvc2.update("hong", new UpdateInfo());
		System.out.println();
		
		msvc2.delete("hong2", "test");
		System.out.println();
	}
}
