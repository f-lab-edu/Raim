package flab.project.repository;

import flab.project.domain.ChatMessage;
import flab.project.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("select cm from ChatMessage cm "
            + "where cm.roomId in "
            + "("
            + "select cp.chatRoom.id from ChatParticipant cp "
            + "where cp.user = :user"
            + ")"
            + "and cm.id > :lastMessageId "
            + "order by cm.id ASC")
    List<ChatMessage> findUnreadMessagesForUser(User user, Long lastMessageId);
}
