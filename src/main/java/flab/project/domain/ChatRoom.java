package flab.project.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ChatRoom implements Serializable {
    private static final long serialVersionUID = 6434598345983495L;

    @Id @GeneratedValue
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ChatRoomType chatRoomType;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatParticipant> chatParticipants = new ArrayList<>();

    private ChatRoom(String name, ChatRoomType chatRoomType) {
        this.name = name;
        this.chatRoomType = chatRoomType;
    }
    public static ChatRoom createPrivateRoom() {
        return new ChatRoom("", ChatRoomType.PRIVATE);
    }

    public static ChatRoom createGroupRoom(String name) {
        return new ChatRoom(name, ChatRoomType.GROUP);
    }
}
