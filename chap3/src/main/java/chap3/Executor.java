package chap3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component //객체화. "executor"이름으로 컨테이너에 저장 = 객체화가 되려면 밑에 Worker가 먼저 객체화돼야함
public class Executor {
	@Autowired //DI 컨테이너중 Worker객체를 찾아서 주입.
	private Worker worker;
	public void addUnit(WorkUnit unit) {
		worker.work(unit);
	}
}
