package websocket;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class EchoHandler extends TextWebSocketHandler implements InitializingBean {
	
	// WebSocketSession : 클라이언트, 채팅중인 1개의 브라우저
	// 현재 채팅중인 모든 브라우저의 세션 저장
	private Set<WebSocketSession> clients = new HashSet<WebSocketSession>();
	// HashMap에서 중복된 클라이언트를 1개로 병합
	@Override // 소켓 접속 완료.
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		super.afterConnectionEstablished(session);
		System.out.println("클라이언트 접속 : " + session.getId());
		clients.add(session);
	}
	
	@Override // 클라이언트에서 메세지가 수신된 경우
	public void handleMessage(WebSocketSession session,WebSocketMessage<?> message) throws Exception {
		String loadMessage = (String)message.getPayload(); // 클라이언트가 전송한 메세지
		System.out.println(session.getId() + " : 클라이언트 메세지 : " + loadMessage);
		clients.add(session);
		for(WebSocketSession s : clients) { // broadcasting
			s.sendMessage(new TextMessage(loadMessage));
  		    // 클라이언트에서 받은 메세지를 모든 클라이언트에게 전송
		}
	}
	
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		super.handleTransportError(session, exception);
		System.out.println("오류 발생 : " + exception.getMessage());
	}
	
	@Override // 브라우저와 접속 종료시
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		super.afterConnectionClosed(session, status);
		System.out.println("클라이언트 접속 해제 : " + status.getReason());
		clients.remove(session);
	}
	
	@Override
	public void afterPropertiesSet() throws Exception{}

} // class
