package flab.project.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ChatParticipant {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CHAT_ROOM_ID")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    private ChatParticipant(ChatRoom chatRoom, User user) {
        this.chatRoom = chatRoom;
        this.user = user;
    }

    public static ChatParticipant createChatParticipant(ChatRoom chatRoom, User user) {
        return new ChatParticipant(chatRoom, user);
    }
}
