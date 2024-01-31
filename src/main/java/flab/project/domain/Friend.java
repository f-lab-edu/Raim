package flab.project.domain;

import flab.project.exception.ExceptionCode;
import flab.project.exception.KakaoException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FRIEND_ID")
    private User friend;

    @Enumerated(EnumType.STRING)
    private FriendStatus friendStatus;

    private LocalDateTime deletedAt;

    @Builder
    public Friend(User user, User friend, FriendStatus friendStatus) {
        this.user = user;
        this.friend = friend;
        this.friendStatus = friendStatus;
    }

    public Friend changeFriendMode(FriendStatus friendStatus) {
        if (friendStatus == FriendStatus.DELETED) {
            checkDeletableFriend(friendStatus);
            checkAlreadyDeleted();
            this.deletedAt = LocalDateTime.now();
        }

        this.friendStatus = friendStatus;

        return this;
    }

    private void checkDeletableFriend(FriendStatus friendStatus) {
        if (this.friendStatus != FriendStatus.HIDDEN) {
            throw new KakaoException(ExceptionCode.BAD_REQUEST);
        }
    }

    private void checkAlreadyDeleted() {
        if (this.friendStatus == FriendStatus.DELETED) {
            throw new KakaoException(ExceptionCode.ALREADY_DELETED_FRIEND);
        }
    }

    public void reactiveFriend() {
        this.friendStatus = FriendStatus.ACTIVE;
    }
}
