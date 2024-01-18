package flab.project.dto;

import flab.project.domain.Friend;
import flab.project.domain.FriendStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FriendResponseDto {

    private Long friendId;

    private FriendStatus friendMode;

    private UserResponseDto userResponse;


    public static FriendResponseDto of(Friend friend) {
        return new FriendResponseDto(friend.getId(), friend.getFriendStatus(), UserResponseDto.of(friend.getFriend()));
    }
}
