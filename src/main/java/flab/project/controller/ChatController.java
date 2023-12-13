package flab.project.controller;

import flab.project.dto.ChatMessageDto;
import flab.project.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatRoomService chatRoomService;

    // 추후 정해진 서버들에서만 이 메서드를 요청할 수 있도록 security 추가해야 함
    @PostMapping("/{userId}")
    public void sendChatMessage(
            @PathVariable Long userId,
            @RequestBody TextMessage textMessage) {
        chatRoomService.sendMessage(userId, textMessage);
    }
}
