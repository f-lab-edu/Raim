package flab.project.controller;

import flab.project.domain.User;
import flab.project.dto.ChatRoomDetailResponseDto;
import flab.project.dto.UserResponseDto;
import flab.project.service.ChatRoomService;
import flab.project.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class ViewController {

    private final UserService userService;
    private final ChatRoomService chatRoomService;

    @GetMapping("/login")
    public String loginPage() {
        return "/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return "redirect:/";
    }

    @GetMapping("/friends")
    public String friendsPage(@AuthenticationPrincipal User user,
                              Model model) {
        List<UserResponseDto> friends = userService.getUserFriends(user.getId());
        model.addAttribute("friends", friends);

        return "/friends";
    }

//    @GetMapping("/friends/chat/{userId}")
//    public String createChatRoom(@AuthenticationPrincipal User user,
//                           @PathVariable Long userId) {
//
//        List<Long> usersId = List.of(user.getId(), userId);
//        ChatRoomDetailResponseDto privateChatRoom = chatRoomService.createChatRoom("", usersId);
//
//        return "redirect:/chat/" + privateChatRoom.getRoomId();
//    }

    @GetMapping("/chat/{roomId}")
    public String chatRoomPage(@AuthenticationPrincipal User user,
                               @PathVariable Long roomId,
                               Model model) {
        if (user != null) {
            model.addAttribute("loginSuccess", true);
        }

        ChatRoomDetailResponseDto chatRoom = chatRoomService.getChatRoom(roomId, user);
        model.addAttribute("loginUser", UserResponseDto.of(user));
        model.addAttribute("chatRoom", chatRoom);

        return "/chatRoom";
    }
}
