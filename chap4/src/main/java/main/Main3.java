package main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/*import annotation.MemberService; */
import xml.Article;
import xml.Member;
import xml.ReadArticleService;
import xml.UpdateInfo;

public class Main3 {
	public static void main(String[] args) {
		String[] config = {"annotation.xml"};
		ApplicationContext ctx = new ClassPathXmlApplicationContext(config);
		ReadArticleService service = ctx.getBean("readArticleService",ReadArticleService.class);
		try {
			Article a1 = service.getArticleAndReadCnt(1);
			System.out.println();
			Article a2 = service.getArticleAndReadCnt(1);
			System.out.println("[main] a1==a2 : " + (a1==a2));
			System.out.println(a1 + " = " + a2 + "\n");
			service.getArticleAndReadCnt(0);
		}catch(Exception e) {
			System.out.println("[main]" + e.getMessage());
		}
		System.out.println("\n=====================================================\n ***** UpdateMemberInfoTrace Aspect 연습 *****\n"
				+ "=====================================================");
		annotation.MemberService msvc = ctx.getBean("memberService",annotation.MemberService.class);
		msvc.regist(new Member());
		System.out.println();
		
		msvc.update("hong", new UpdateInfo());
		System.out.println();
		
		msvc.delete("hong2", "test");
		
		System.out.println("\n========================================================\n ***** main.MemberService 메서드 호출 *****\n"
				+ "========================================================");
		
		main.MemberService msvc2 = ctx.getBean("memberService2",main.MemberService.class);
		msvc2.regist(new Member());
		msvc2.update("hong", new UpdateInfo());
		msvc2.delete("hong2", "test");
	}
}
