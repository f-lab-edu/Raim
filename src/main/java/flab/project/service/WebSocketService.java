package flab.project.service;

import flab.project.dto.ChatMessageRequestDto;
import flab.project.dto.ChatMessageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendMessageToPrivateRoom(ChatMessageRequestDto message) {
        messagingTemplate.convertAndSend("/sub/chat/private-room/" + message.getRoomId(), ChatMessageResponseDto.of(message));
    }

    public void sendMessageToGroupRoom(ChatMessageRequestDto message) {
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), ChatMessageResponseDto.of(message));
    }
}
