package flab.project.dto;

import flab.project.domain.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatMessageResponseDto {

    private Long id;
    private Long roomId;
    private String sender;
    private String message;

    public static ChatMessageResponseDto of(ChatMessage chatMessage) {
        return new ChatMessageResponseDto(chatMessage.getId(), chatMessage.getRoomId(), chatMessage.getSender(),
                chatMessage.getMessage());
    }
}
