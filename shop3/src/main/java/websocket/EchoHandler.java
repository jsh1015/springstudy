package websocket;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class EchoHandler extends TextWebSocketHandler{
	//세션 중복불가.: 접속된 클라이언트 목록
	private Set<WebSocketSession> clients = new HashSet<WebSocketSession>();
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception{
		super.afterConnectionEstablished(session); //연결
		System.out.println("클라이언트 접속" + session.getId());
		clients.add(session); //set객체에 등록됨
	}
	@Override //클라이언트에서 메세지 수신						엔터키를 누르면 입력한 내용이 넘어옴
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception{
		String loadMessage = (String)message.getPayload();
		//loadMessage : 클라이언트가 전송한 메세지 값
		System.out.println(session.getId() +" 클라이언트 메세지:"+loadMessage);
		clients.add(session);//또한번 세션 추가, 없어지면 다시 연결해 주려고(어차피 중복 불가) 원래있으면 안됨
		//접속된 클라이언트 들에게 수신된 메세지 전송 : 화면에 보이게 
		for(WebSocketSession s : clients) {//브로드 캐스트
			s.sendMessage(new TextMessage(loadMessage));
		}
	}
	@Override //오류 발생시. 
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception{
		super.handleTransportError(session, exception);
		System.out.println("오류발생 : " + exception.getMessage());
	}
	@Override //연결 종료
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception{
		super.afterConnectionClosed(session, status);
		System.out.println("클라이언트 접속 해제 " + status.getReason());
		clients.remove(session);
	}
}
