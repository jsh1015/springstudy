package chap3;

import org.springframework.context.support.GenericXmlApplicationContext;

public class Main1 {
	public static void main(String[] args) {
		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext("classpath:annotation.xml");
		Executor exec = ctx.getBean("executor",Executor.class); //Executor.java에있는 executor
		exec.addUnit(new WorkUnit());
		exec.addUnit(new WorkUnit());	//메인에서 실행되는것
		
		HomeController home = ctx.getBean("homeController",HomeController.class); //ctx가 객체를 만드는 순간에 호출되므로 먼저 나옴
		home.checkSensorAndAlarm();
		System.out.println("============= 침입 없음 =============");
		System.out.println("=================================");
		//창문에 침입함
		InfraredRaySensor sensor = ctx.getBean("windowSensor",InfraredRaySensor.class);
		sensor.foundObject();
		home.checkSensorAndAlarm();
//		sensor = new InfraredRaySensor("현관센서");
		sensor.foundObject();
		home.checkSensorAndAlarm();
	}
}