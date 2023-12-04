package flab.project.dto;

import flab.project.domain.ChatRoom;
import flab.project.domain.ChatRoomType;
import flab.project.domain.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class ChatRoomResponseDto {

    private Long roomId;
    private String name;
    private ChatRoomType chatRoomType;
    private List<UserResponseDto> users;

    private ChatRoomResponseDto(Long roomId, String name, ChatRoomType chatRoomType, List<UserResponseDto> users) {
        this.roomId = roomId;
        this.name = name;
        this.chatRoomType = chatRoomType;
        this.users = users;
    }

    public static ChatRoomResponseDto of(ChatRoom chatRoom, List<User> users) {
        return new ChatRoomResponseDto(chatRoom.getId(), chatRoom.getName(), chatRoom.getChatRoomType(),
                users.stream().map(user -> UserResponseDto.of(user)).collect(
                        Collectors.toList()));
    }
}
