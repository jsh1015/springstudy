package chap3;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component //객체화. 이름은 "worker" 로 컨테이너에 저장됨
@Scope(value="prototype",proxyMode=ScopedProxyMode.TARGET_CLASS) 
//만들때마다 다른 객체로 사용하게 하려면/ 일회성 객체로 주입됨
public class Worker {
	public void work(WorkUnit unit) {
		System.out.println(this + ":work :" + unit);
	}
}