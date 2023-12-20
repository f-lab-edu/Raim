package flab.project.repository;

import flab.project.domain.ChatParticipant;
import flab.project.domain.ChatRoom;
import flab.project.domain.ChatRoomType;
import flab.project.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {

    @Query("SELECT cp1.chatRoom FROM ChatParticipant cp1, ChatParticipant cp2 "
            + "WHERE cp1.user = :user1 AND cp2.user = :user2 AND cp1.chatRoom = cp2.chatRoom "
            + "AND cp1.chatRoom.chatRoomType = :chatRoomType AND cp1.chatRoom.chatRoomType = :chatRoomType")
    Optional<ChatRoom> findPrivateChatRoom(@Param("user1") User user1,
                                           @Param("user2") User user2,
                                           @Param("chatRoomType")ChatRoomType chatRoomType);
}
