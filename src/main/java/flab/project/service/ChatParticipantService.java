package flab.project.service;

import flab.project.domain.ChatParticipant;
import flab.project.domain.ChatRoom;
import flab.project.domain.User;
import flab.project.repository.ChatParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ChatParticipantService {

    private final ChatParticipantRepository chatParticipantRepository;

    @Transactional
    public void setChatParticipant(ChatRoom chatRoom, User user) {
        ChatParticipant chatParticipant = ChatParticipant.createChatParticipant(chatRoom, user);

        chatParticipantRepository.save(chatParticipant);
    }
}
