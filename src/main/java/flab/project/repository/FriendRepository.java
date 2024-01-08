package flab.project.repository;

import flab.project.domain.Friend;
import flab.project.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepository extends JpaRepository<Friend,Long> {

    boolean existsByUserAndFriend(User user, User friend);
}
