package xml;

public class MemberService {
	public void regist(Member member) {
		System.out.println("MemberService.regist() 메서드 실행");
	}
	public boolean update(String memberId, UpdateInfo info) { //pointcut="args(id,info)"의 대상이되는 메서드
		System.out.println("MemberService.update() 메서드 실행");
		return true;
	}
	public boolean delete(String id, String str) {
		System.out.println("MemberService.delete() 메서드 실행");
		return false;
	}
}
