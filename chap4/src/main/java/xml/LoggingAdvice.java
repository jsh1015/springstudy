package xml;

public class LoggingAdvice {
	public void before() {
		System.out.println("[LA]메서드 실행 전 전처리 수행 기능 1");
	}
	//핵심로직 정상 종료 후. 리턴값:ret
	public void afterReturning(Object ret) {
		System.out.println("[LA]메서드 정상 처리 후 수행 함. 2 ret=" + ret);
	}
	//핵심로직 예외 종료 후. 리턴값:ex
	public void afterThrowing(Throwable ex) {
		System.out.println("[LA]메서드 예외 발행 후 수행함. 3  발생 예외 : " +ex.getMessage());
	}
	//핵심로직 종료
	public void afterFinally() {
		System.out.println("[LA]메서드 실행 후 수행함. 4 ");
	}
//	aop2.xml에있는 메서드명
}
