package flab.project.controller;

import flab.project.dto.CommonResponseDto;
import flab.project.dto.FriendResponseDto;
import flab.project.security.userDetails.UserContext;
import flab.project.service.FriendService;
import flab.project.util.ValidationUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friend")
public class FriendController {

    private final FriendService friendService;

    @GetMapping("/phoneNumber")
    public void plusFriendByPhoneNumber(
            @RequestParam String value,
            @AuthenticationPrincipal UserContext user
    ) {
        ValidationUtils.validatePhoneNumber(value);
        friendService.plusFriendByPhoneNumber(user.getUser(), value);
    }

    @GetMapping("/email")
    public void plusFriendByEmail(
            @RequestParam String value,
            @AuthenticationPrincipal UserContext user
    ) {
        ValidationUtils.validateEmail(value);
        friendService.plusFriendByEmail(user.getUser(), value);
    }

    // DB에 User로 인덱스를 걸어 최적화할 수 있는 부분이 있지 않을까?
    @GetMapping("/friends")
    public CommonResponseDto getUserFriends(
            @AuthenticationPrincipal UserContext user
    ) {
        List<FriendResponseDto> friends = friendService.getUserFriends(user.getUser());

        return CommonResponseDto.of("친구 목록", friends);
    }

    @DeleteMapping("/{friendId}")
    public void deleteFriendByEmail(
            @PathVariable Long friendId) {
        friendService.deleteFriend(friendId);
    }

    @GetMapping("/{friendId}")
    public void changeBlockFriendById(
            @PathVariable Long friendId,
            @RequestParam boolean isBlock
    ) {
        friendService.changeBlockFriend(friendId, isBlock);
    }
}
