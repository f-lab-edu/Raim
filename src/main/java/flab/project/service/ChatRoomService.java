package flab.project.service;

import flab.project.domain.ChatParticipant;
import flab.project.domain.ChatRoom;
import flab.project.domain.ChatRoomType;
import flab.project.domain.User;
import flab.project.dto.PrivateChatRoomResponseDto;
import flab.project.exception.ExceptionCode;
import flab.project.exception.KakaoException;
import flab.project.repository.ChatParticipantRepository;
import flab.project.repository.ChatRoomRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatParticipantService chatParticipantService;
    private final UserService userService;

    @Transactional
    public PrivateChatRoomResponseDto createPrivateChatRoom(Long user1Id, Long user2Id) {

        User user1 = userService.getUser(user1Id);
        User user2 = userService.getUser(user2Id);

        Optional<ChatRoom> findChatRoom = chatParticipantRepository.findPrivateChatRoom(user1, user2, ChatRoomType.PRIVATE);
        if (findChatRoom.isPresent()) {
            return PrivateChatRoomResponseDto.of(findChatRoom.get(), user1.getId(), user2.getId());
        }

        ChatRoom chatRoom = ChatRoom.createPrivateRoom();

        chatParticipantService.setChatParticipant(chatRoom, user1);
        chatParticipantService.setChatParticipant(chatRoom, user2);

        chatRoomRepository.save(chatRoom);

        return PrivateChatRoomResponseDto.of(chatRoom, user1.getId(), user2.getId());
    }

    public PrivateChatRoomResponseDto getChatRoom(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new KakaoException(ExceptionCode.CHATROOM_NOT_FOUND));
        List<ChatParticipant> chatParticipants = chatRoom.getChatParticipants();

        return PrivateChatRoomResponseDto.of(chatRoom, chatParticipants.get(0).getId(), chatParticipants.get(1).getId());
    }

}
