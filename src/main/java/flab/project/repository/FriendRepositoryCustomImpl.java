package flab.project.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import flab.project.domain.Friend;
import flab.project.domain.FriendStatus;
import flab.project.domain.QFriend;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FriendRepositoryCustomImpl implements FriendRepositoryCustom{

    private final JPAQueryFactory query;

    @Override
    public List<Friend> findFriend(FriendStatus friendStatus) {
        QFriend friend = QFriend.friend1;
        return query
                .select(friend)
                .from(friend)
                .leftJoin(friend.friend)
                .fetchJoin()
                .where(friend.friendStatus.eq(friendStatus))
                .fetch();
    }
}
