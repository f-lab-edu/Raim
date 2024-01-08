package flab.project.controller;

import flab.project.security.userDetails.UserContext;
import flab.project.service.FriendService;
import flab.project.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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
            @AuthenticationPrincipal UserContext user) {
        ValidationUtils.validateEmail(value);
        friendService.plusFriendByEmail(user.getUser(), value);
    }
}
