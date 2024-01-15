package flab.project.dto;

import flab.project.domain.ChatRoom;
import flab.project.domain.ChatRoomType;
import lombok.Getter;

@Getter
public class ChatRoomResponseDto {

    private Long roomId;
    private String name;
    private ChatRoomType chatRoomType;

    private ChatRoomResponseDto(Long roomId, String name, ChatRoomType chatRoomType) {
        this.roomId = roomId;
        this.name = name;
        this.chatRoomType = chatRoomType;
    }

    public static ChatRoomResponseDto of(ChatRoom chatRoom) {
        return new ChatRoomResponseDto(chatRoom.getId(), chatRoom.getName(), chatRoom.getChatRoomType());
    }
}
