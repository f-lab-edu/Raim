package flab.project.dto;

import static java.util.stream.Collectors.toList;

import flab.project.domain.ChatRoom;
import flab.project.domain.ChatRoomType;
import flab.project.domain.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class ChatRoomDetailResponseDto {

    private Long roomId;
    private String name;
    private ChatRoomType chatRoomType;
    private List<UserResponseDto> users;

    private ChatRoomDetailResponseDto(Long roomId, String name, ChatRoomType chatRoomType, List<UserResponseDto> users) {
        this.roomId = roomId;
        this.name = name;
        this.chatRoomType = chatRoomType;
        this.users = users;
    }

    public static ChatRoomDetailResponseDto of(ChatRoom chatRoom, List<User> users) {
        return new ChatRoomDetailResponseDto(chatRoom.getId(), chatRoom.getName(), chatRoom.getChatRoomType(),
                users.stream().map(UserResponseDto::of).toList());
    }
}
