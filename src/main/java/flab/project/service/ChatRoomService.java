package flab.project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import flab.project.domain.ChatMessage;
import flab.project.domain.ChatParticipant;
import flab.project.domain.ChatRoom;
import flab.project.domain.ChatRoomType;
import flab.project.domain.ServerInfo;
import flab.project.domain.User;
import flab.project.dto.ChatMessageDto;
import flab.project.dto.ChatRoomResponseDto;
import flab.project.exception.ExceptionCode;
import flab.project.exception.KakaoException;
import flab.project.repository.ChatMessageRepository;
import flab.project.repository.ChatParticipantRepository;
import flab.project.repository.ChatRoomRepository;
import flab.project.repository.WebSocketSessionRepository;
import flab.project.util.ValidationUtils;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatParticipantService chatParticipantService;
    private final WebSocketSessionRepository webSocketSessionRepository;
    private final UserService userService;
    private final ObjectMapper objectMapper;


    public boolean existPrivateChatRoom(List<Long> usersId) {
        List<User> users = usersId.stream().map(userId ->
                userService.getUser(userId)).toList();

        User user1 = users.get(0);
        User user2 = users.get(1);

        Optional<ChatRoom> findChatRoom = chatParticipantRepository.findPrivateChatRoom(user1, user2,
                ChatRoomType.PRIVATE);

        return findChatRoom.isPresent();
    }

    @Transactional
    public ChatRoomResponseDto createPrivateChatRoom(List<Long> usersId) {
        List<User> users = usersId.stream().map(userId ->
                userService.getUser(userId)).toList();

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
    public void sendMessage(User loginUser, TextMessage message) {
        ChatMessage chatMessage = getChatMessage(message);

        ChatRoom chatRoom = chatRoomRepository.findById(chatMessage.getRoomId())
                .orElseThrow(() -> new KakaoException(ExceptionCode.CHATROOM_NOT_FOUND));

        List<User> users = chatRoom.getChatParticipants().stream().map(ChatParticipant::getUser)
                .collect(Collectors.toList());

        ValidationUtils.validateSendMessage(loginUser, users);

        for (User user: users) {
            ConcurrentWebSocketSessionDecorator userWebSocketSession = webSocketSessionRepository.getWebSocketSession(user.getId());

            // User가 현재 서버에 세션이 없을때를 고려해야 함.
            if (userWebSocketSession == null) {
                // 1. User의 웹소켓 세션이 다른 서버에 있을 때
                if (webSocketSessionRepository.containUserSession(user.getId())) {
                    ServerInfo serverInfo = webSocketSessionRepository.getUserSession(user.getId());

                    String url = "http://" + serverInfo.getAddress() + ":" + serverInfo.getPort() + "/api/chat/" + user.getId();

                    // 이렇게 동기적으로 처리하는 방식이 옳은 방식일까?
                    try {
                        RestTemplate restTemplate = new RestTemplate();

                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);

                        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(message), headers);

                        restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

                    } catch (JsonProcessingException e) {
                        throw new KakaoException(ExceptionCode.SERVER_ERROR);
                    }
                }

                // 2. User의 웹소켓 세션이 아무곳에도 없을 때
            }

            try {
                userWebSocketSession.sendMessage(message);
            } catch (IOException e) {
                throw new KakaoException(ExceptionCode.SERVER_ERROR);
            }

            chatMessageRepository.save(chatMessage);
        }
    }

    @Transactional
    public void sendMessage(Long userId, TextMessage textMessage) {
        ConcurrentWebSocketSessionDecorator userWebSocketSession = webSocketSessionRepository.getWebSocketSession(userId);

        try {
            userWebSocketSession.sendMessage(textMessage);
        } catch (IOException e) {
            throw new KakaoException(ExceptionCode.SERVER_ERROR);
        }
    }

    private ChatMessage getChatMessage(TextMessage textMessage) {
        try {
            ChatMessageDto chatMessageDto = objectMapper.readValue(textMessage.getPayload(), ChatMessageDto.class);
            return ChatMessage.of(chatMessageDto);
        } catch (JsonProcessingException e) {
            throw new KakaoException(ExceptionCode.SERVER_ERROR);
        }
    }
}
