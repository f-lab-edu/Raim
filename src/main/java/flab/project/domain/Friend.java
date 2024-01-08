package flab.project.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Friend extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "FRIEND_ID")
    private User friend;

    private boolean isBlock;

    @Builder
    public Friend(User user, User friend, boolean isBlock) {
        this.user = user;
        this.friend = friend;
        this.isBlock = isBlock;
    }

    public void changeBlockMode(boolean isBlock) {
        this.isBlock = isBlock;
    }
}
