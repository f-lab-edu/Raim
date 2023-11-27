package flab.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChatMessageResponseDto {

    private String sender;
    private String message;


    public static ChatMessageResponseDto of(ChatMessageRequestDto chatMessageRequestDto) {
        return new ChatMessageResponseDto(chatMessageRequestDto.getSender(), chatMessageRequestDto.getMessage());
    }
}
