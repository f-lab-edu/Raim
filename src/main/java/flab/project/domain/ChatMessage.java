package flab.project.domain;

import flab.project.dto.ChatMessageDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ChatMessage extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long roomId;

    private String sender;

    private String message;

    public ChatMessage(Long roomId, String sender, String message) {
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
    }

    public static ChatMessage of(ChatMessageDto chatMessageDto) {

        return new ChatMessage(chatMessageDto.getRoomId(), chatMessageDto.getSender(), chatMessageDto.getMessage());
    }
}
