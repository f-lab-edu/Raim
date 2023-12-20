package flab.project.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import flab.project.domain.ServerInfo;
import flab.project.domain.User;
import flab.project.dto.ChatMessageDto;
import flab.project.repository.WebSocketSessionRepository;
import flab.project.service.ChatRoomService;
import flab.project.util.AddressUtils;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ChatRoomService chatRoomService;
    private final WebSocketSessionRepository webSocketSessionRepository;
    private final AddressUtils addressUtils;

    @Value("${server.port}")
    private int port;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message){
        User user = getUserInfo(session);
        chatRoomService.sendMessage(user, message);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        User user = getUserInfo(session);
        webSocketSessionRepository.saveUserSession(user.getId(), session, ServerInfo.of(addressUtils.getMyExternalIP(), port));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        User user = getUserInfo(session);
        webSocketSessionRepository.deleteUserSession(user.getId());
    }

    private User getUserInfo(WebSocketSession session) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) session.getPrincipal();
        User user = (User) token.getPrincipal();

        return user;
    }
}
