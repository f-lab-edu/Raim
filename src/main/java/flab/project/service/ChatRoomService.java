package flab.project.service;

import flab.project.domain.ChatParticipant;
import flab.project.domain.ChatRoom;
import flab.project.domain.ChatRoomType;
import flab.project.domain.User;
import flab.project.dto.ChatRoomResponseDto;
import flab.project.exception.ExceptionCode;
import flab.project.exception.KakaoException;
import flab.project.repository.ChatParticipantRepository;
import flab.project.repository.ChatRoomRepository;
import flab.project.repository.WebSocketSessionRepository;
import flab.project.util.ValidationUtils;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatParticipantService chatParticipantService;
    private final WebSocketSessionRepository webSocketSessionRepository;
    private final UserService userService;


    public boolean existPrivateChatRoom(List<Long> usersId) {
        List<User> users = usersId.stream().map(userId ->
                userService.getUser(userId)).collect(Collectors.toList());

        User user1 = users.get(0);
        User user2 = users.get(1);

        Optional<ChatRoom> findChatRoom = chatParticipantRepository.findPrivateChatRoom(user1, user2,
                ChatRoomType.PRIVATE);

        return findChatRoom.isPresent();
    }

    @Transactional
    public ChatRoomResponseDto createPrivateChatRoom(List<Long> usersId) {
        List<User> users = usersId.stream().map(userId ->
                userService.getUser(userId)).collect(Collectors.toList());

        User user1 = users.get(0);
        User user2 = users.get(1);

        Optional<ChatRoom> findChatRoom = chatParticipantRepository.findPrivateChatRoom(user1, user2,
                ChatRoomType.PRIVATE);
        if (findChatRoom.isPresent()) {
            return ChatRoomResponseDto.of(findChatRoom.get(), users);
        }

        ChatRoom chatRoom = ChatRoom.createChatRoom("", ChatRoomType.PRIVATE);

        chatParticipantService.setChatParticipant(chatRoom, user1);
        chatParticipantService.setChatParticipant(chatRoom, user2);

        chatRoomRepository.save(chatRoom);

        return ChatRoomResponseDto.of(chatRoom, users);
    }

    public ChatRoomResponseDto getChatRoom(Long roomId, User loginUser) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new KakaoException(ExceptionCode.CHATROOM_NOT_FOUND));
        List<ChatParticipant> chatParticipants = chatRoom.getChatParticipants();
        List<User> users = chatParticipants.stream()
                .map(ChatParticipant::getUser).collect(
                        Collectors.toList());

        ValidationUtils.validateSendMessage(loginUser, users);

        return ChatRoomResponseDto.of(chatRoom, users);
    }

    @Transactional
    public void sendMessage(Long roomId, User loginUser, TextMessage message) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new KakaoException(ExceptionCode.CHATROOM_NOT_FOUND));

        List<User> users = chatRoom.getChatParticipants().stream().map(ChatParticipant::getUser)
                .collect(Collectors.toList());

        ValidationUtils.validateSendMessage(loginUser, users);

        for (User user: users) {
            WebSocketSession userWebSocketSession = webSocketSessionRepository.getWebSocketSession(user.getId());

            // User가 현재 서버에 세션이 없을때를 고려해야 함.
            // 1. User의 웹소켓 세션이 다른 서버에 있을 때
            // 2. User의 웹소켓 세션이 아무곳에도 없을

            try {
                userWebSocketSession.sendMessage(message);
            } catch (IOException e) {
                throw new KakaoException(ExceptionCode.SERVER_ERROR);
            }
        }
    }
}
