package flab.project.controller;

import flab.project.dto.ChatMessageRequestDto;
import flab.project.publish.RedisPublisher;
import flab.project.service.ChatRoomService;
import flab.project.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final WebSocketService webSocketService;

    @MessageMapping("/chat/private-message")
    public void sendPrivateMessage(ChatMessageRequestDto message) {
        webSocketService.sendMessageToPrivateRoom(message);
    }

    @MessageMapping("/chat/group-message")
    public void sendGroupMessage(ChatMessageRequestDto message) {
        webSocketService.sendMessageToGroupRoom(message);
    }

}
