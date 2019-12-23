package chap3;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

//homecontroller 이름의 객체로 생성되고, 컨테이너에 저장
@Component
public class HomeController {
	private AlarmDevice alarmDevice;
	private Viewer viewer;
	
	@Resource(name="camera1")	//@Resource 이름으로 객체를 주입
	private Camera camera1;
	@Resource(name="camera2")
	private Camera camera2;
	@Resource(name="camera3")
	private Camera camera3;
	@Resource(name="camera4")
	private Camera camera4;
	
	private List<InfraredRaySensor> sensors;
	
//				없어도 에러가 안남
	@Autowired(required=false)	//Recorder 객체가 없으면 null로 주입
	private Recorder recorder;
	@Autowired	//현재나의 컨테이너에서 AlarmDevice 객체와 , Viewer 객체를 주입해라
							//smsslarmdevice가 주입	monitorviewer가 주입
	public void prepare(AlarmDevice alarmDevice, Viewer viewer) { //둘다 인터페이스이므로 구현클래스가 필요함
		//viewer객체가 두개이면 에러남
		this.alarmDevice = alarmDevice;
		this.viewer = viewer;
	}
	
	@PostConstruct	//객체 생성시 모든 객체의 준비완료 후, 객체 생성 이후 init()을 호출해라 
	public void init() {
		System.out.println("<init() 메서드 호출>");
		viewer.add(camera1); viewer.add(camera2);
		viewer.add(camera3); viewer.add(camera4);
		viewer.draw();
	}
	
	@Autowired
	@Qualifier("intrusionDetection") //별명 설정, 주석처리하면 다가져와서 출력하게 됨
	public void setSensors(List<InfraredRaySensor> sensors) {
		System.out.println("<setSensors() 메서드 호출>");
		this.sensors = sensors;
		for(InfraredRaySensor s : sensors) {
			System.out.println("센서 등록 : " +s);
		}
	}
	public void checkSensorAndAlarm() {
		for(InfraredRaySensor s : sensors) {
			if(s.isObjectFounded()) { //true일때 출력
				alarmDevice.alarm(s.getName());
			}
		}
	}
}
/*
 * 기본어노테이션
 * 1. 객체 생성 : @Component
 * 		xml : <context:component-scan base-package="chap3" /> 를 같이 사용해야 함
 * 2. 생성된 객체를 사용하는 것 (객체 주입) DI의 핵심
 * 		@Autowired : 객체선택의 기준 자료형임
 * 					(required = false) : 객체가 없는 경우 가능하게 
 * 		@Resource(이름) : 객체 중 이름에 해당 하는 객체를 주입 
 * 		@Required  : 객체선택의 기준 자료형임. 반드시 객체가 필요함
 * 3. 그외
 * 		@PostConstruct : 객체 생성이 완료된 후에 호출 되는 메서드의 위에 설정
 * 		@Qualifier : 객체의 이름에 별명을 설정. @Autowired와 함께 사용됨
 * 		@Scope(..)	: 생성된 객체의 지속가능한 영역 설정.
 * 					 @Component 와 함께 사용됨 
*/
