package flab.project.dto;

import flab.project.domain.Friend;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FriendResponseDto {

    private Long friendId;

    private UserResponseDto userResponse;


    public static FriendResponseDto of(Friend friend) {
        return new FriendResponseDto(friend.getId(), UserResponseDto.of(friend.getFriend()));
    }
}
