package flab.project.dto;

import flab.project.domain.ChatRoom;
import lombok.Getter;

@Getter
public class PrivateChatRoomResponseDto {

    private Long roomId;
    private String name;
    private Long user1Id;
    private Long user2Id;

    private PrivateChatRoomResponseDto(Long roomId, String name, Long user1Id, Long user2Id) {
        this.roomId = roomId;
        this.name = name;
        this.user1Id = user1Id;
        this.user2Id = user2Id;
    }

    public static PrivateChatRoomResponseDto of(ChatRoom chatRoom, Long user1Id, Long user2Id) {
        return new PrivateChatRoomResponseDto(chatRoom.getId(), chatRoom.getName(), user1Id, user2Id);
    }
}
