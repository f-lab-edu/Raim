package flab.project.controller;

import flab.project.dto.PrivateChatRoomRequestDto;
import flab.project.dto.PrivateChatRoomResponseDto;
import flab.project.dto.UserResponseDto;
import flab.project.service.ChatRoomService;
import flab.project.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
@RequestMapping("/friends")
public class FriendController {
    private final UserService userService;
    private final ChatRoomService chatRoomService;

    @GetMapping
    public String getFriendList(Model model) {
        // user의 로그인 정보로 친구 목록찾기
        List<UserResponseDto> users = userService.findUserFriends(0L);
        model.addAttribute("users", users);

        return "/friends";
    }

    @PostMapping("/private")
    @ResponseBody
    public PrivateChatRoomResponseDto createPrivateChatRoom(@RequestBody PrivateChatRoomRequestDto privateChatRoomRequestDto) {

        PrivateChatRoomResponseDto privateChatRoom = chatRoomService.createPrivateChatRoom(
                privateChatRoomRequestDto.getUser1Id(),
                privateChatRoomRequestDto.getUser2Id());
        return privateChatRoom;
    }

    @RequestMapping("/chat/private-room/{roomId}")
    public String getPrivateChatRoom(@PathVariable Long roomId,
                                     Model model) {
        PrivateChatRoomResponseDto chatRoomResponseDto = chatRoomService.getChatRoom(roomId);
        model.addAttribute(chatRoomResponseDto);

        return "/chat-room";
    }
}
