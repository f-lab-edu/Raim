package flab.project.repository;

import flab.project.domain.Friend;
import flab.project.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepository extends JpaRepository<Friend,Long> {

    boolean existsByUserAndFriend(User user, User friend);

    List<Friend> findByUser(User user);
}
