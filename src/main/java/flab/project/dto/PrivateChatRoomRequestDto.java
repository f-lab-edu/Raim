package flab.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PrivateChatRoomRequestDto {

    private Long user1Id;
    private Long user2Id;
}
