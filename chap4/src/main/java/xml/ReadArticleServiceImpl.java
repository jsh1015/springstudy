package xml;

public class ReadArticleServiceImpl implements ReadArticleService{
	public Article getArticleAndReadCnt(int id) throws Exception {
		System.out.println("getArticleAndReadCnt("+id+") 호출");
		if(id==0) {
			throw new Exception("id는 0이 안됨");
			//예외객체 발생(예외처리)
		}
		return new Article(id);
	}
}