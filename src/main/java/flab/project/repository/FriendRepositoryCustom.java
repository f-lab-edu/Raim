package flab.project.repository;

import flab.project.domain.Friend;
import flab.project.domain.FriendStatus;
import java.util.List;
import org.springframework.stereotype.Repository;

public interface FriendRepositoryCustom {
    List<Friend> findFriend(FriendStatus friendStatus);
}
