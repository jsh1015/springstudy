package main;

import org.springframework.stereotype.Component;

import xml.Member;
import xml.UpdateInfo;

@Component("memberService2")
public class MemberService {
	public void regist(Member member) {
		System.out.println("main.MemberService.regist() 메서드 실행");
	}
	public boolean update(String memberId, UpdateInfo info) { //pointcut="args(id,info)"의 대상이되는 메서드
		System.out.println("main.MemberService.update() 메서드 실행");
		return true;
	}
	public boolean delete(String id, String str) {
		System.out.println("main.MemberService.delete() 메서드 실행");
		return false;
	}
}
