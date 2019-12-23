package chap2;

public class WriteImpl {
	private ArticleDao dao;
	public WriteImpl(ArticleDao dao) { //생성자, 생성자없을시 default기본생성자가 있음
		this.dao = dao;
	}
	public void write() {
		System.out.println("WriteImpl.write 메서드 호출");
		dao.insert();
	}
}