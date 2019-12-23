package annotation;

import org.springframework.stereotype.Component;

import xml.Article;
import xml.ReadArticleService;

@Component("readArticleService") //이름을 지정해주지 않으면 readarticleserviceimpl 로 됨
public class ReadArticleServiceImpl implements ReadArticleService{
	public Article getArticleAndReadCnt(int id) throws Exception{
		if(id==0) {
			throw new Exception("id는 0이 안됨");
		}
		return new Article(id);
	}
}
