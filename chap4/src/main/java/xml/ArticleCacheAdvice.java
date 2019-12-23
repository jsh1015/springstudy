package xml;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;

public class ArticleCacheAdvice {
	private Map<Integer,Article> cache = new HashMap<Integer,Article>();
	public Object cache(ProceedingJoinPoint joinPoint) throws Throwable{
		System.out.println("[ACA] cache before 실행");
		Integer id = (Integer) joinPoint.getArgs()[0]; //핵심로직에있는 매개변수를 가져오는것
		Article article = cache.get(id);
		if(article!=null) {
			System.out.println("[ACA] cache에서 Article["+id+"] 가져옴");
			return article; //핵심로직으로 보내지 않고 main으로 리턴해버림(이미있으니까)
		}
		Object ret = joinPoint.proceed(); //logging의 la가 먼저 실행됨
		System.out.println("[ACA] cache after 실행"); //id가 0일때 실행되지 않음(예외발생했기 때문에)
		if(ret!=null && ret instanceof Article) {
			cache.put(id,(Article)ret);
			System.out.println("[ACA] cache에 Article["+id+"] 추가함");
		}
		return ret;
	}
}
