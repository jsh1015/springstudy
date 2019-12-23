package chap1;

public class Greeter {
	private String format;
	public String greet(String guest) {
		return String.format(format, guest);
					//		%s을 시작합니다,"스프링"
	}
	
	//format : %s을 시작합니다.
	//직접만드는게 아닌 설정에 의해 들어가는것 (DI)
	public void setFormat(String format) {
		this.format = format;
	}
}