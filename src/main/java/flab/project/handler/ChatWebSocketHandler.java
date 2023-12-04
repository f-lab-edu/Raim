package flab.project.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import flab.project.domain.ServerInfo;
import flab.project.domain.User;
import flab.project.dto.ChatMessageDto;
import flab.project.repository.WebSocketSessionRepository;
import flab.project.service.ChatRoomService;
import java.net.InetSocketAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final ObjectMapper objectMapper;
    private final ChatRoomService chatRoomService;
    private final WebSocketSessionRepository webSocketSessionRepository;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        User user = getUserInfo(session);
        ChatMessageDto chatMessageDto = objectMapper.readValue(message.getPayload(), ChatMessageDto.class);
        chatRoomService.sendMessage(chatMessageDto.getRoomId(), user, message);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        User user = getUserInfo(session);
        InetSocketAddress localAddress = session.getLocalAddress();
        webSocketSessionRepository.saveUserSession(user.getId(), session, ServerInfo.of(localAddress.getAddress(), localAddress.getPort()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        User user = getUserInfo(session);

        webSocketSessionRepository.deleteUserSession(user.getId());
    }

    private User getUserInfo(WebSocketSession session) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) session.getPrincipal();
        User user = (User) token.getPrincipal();

        return user;
    }
}
