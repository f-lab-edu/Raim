package flab.project.controller;

import flab.project.domain.User;
import flab.project.dto.ChatMessageResponseDto;
import flab.project.dto.ChatRoomRequestDto;
import flab.project.dto.ChatRoomDetailResponseDto;
import flab.project.dto.ChatRoomResponseDto;
import flab.project.dto.CommonResponseDto;
import flab.project.security.userDetails.UserContext;
import flab.project.service.ChatMessageService;
import flab.project.service.ChatRoomService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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
    private final ChatMessageService chatMessageService;

    // 추후 정해진 서버들에서만 이 메서드를 요청할 수 있도록 security 추가해야 함
    @PostMapping("/{userId}")
    public void sendChatMessage(
            @PathVariable Long userId,
            @RequestBody TextMessage textMessage) {
        chatRoomService.sendMessage(userId, textMessage);
    }

    @GetMapping("/message/{lastMessageId}")
    public CommonResponseDto<List<ChatMessageResponseDto>> getUnreadMessage(
            @PathVariable Long lastMessageId,
            @AuthenticationPrincipal User user) {

        List<ChatMessageResponseDto> unreadMessages = chatMessageService.getUnreadMessage(user, lastMessageId);

        return CommonResponseDto.of("읽지 않은 메시지", unreadMessages);
    }

    @PostMapping("/")
    public CommonResponseDto<ChatRoomDetailResponseDto> createChatRoom(
            ChatRoomRequestDto chatRoomRequestDto
    ) {

        ChatRoomDetailResponseDto chatRoomDetailResponseDto = chatRoomService.createChatRoom(chatRoomRequestDto);
        return CommonResponseDto.of("채팅방 정보", chatRoomDetailResponseDto);
    }

    @PostMapping("/{roomId}")
    public CommonResponseDto<ChatRoomDetailResponseDto> inviteUser(
            @PathVariable Long roomId,
            @RequestBody List<Long> usersId
    ) {
        ChatRoomDetailResponseDto chatRoomDetailResponseDto = chatRoomService.inviteUser(roomId, usersId);

        return CommonResponseDto.of("초대를 완료한 채팅방 정보", chatRoomDetailResponseDto);
    }

    @GetMapping("/rooms")
    public CommonResponseDto<List<ChatRoomResponseDto>> getChatRooms(
            @AuthenticationPrincipal UserContext user,
            Pageable pageable
    ) {
        List<ChatRoomResponseDto> chatRooms = chatRoomService.getChatRooms(user.getUsername(), pageable);

        return CommonResponseDto.of("사용자가 참여한 채팅룸", chatRooms);
    }
}
