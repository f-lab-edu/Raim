package flab.project.service;

import flab.project.domain.User;
import flab.project.dto.ChatMessageResponseDto;
import flab.project.repository.ChatMessageRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public List<ChatMessageResponseDto> getUnreadMessage(User user, Long lastMessageId) {
        return chatMessageRepository.findUnreadMessagesForUser(user, lastMessageId).stream().map(message -> ChatMessageResponseDto.of(message)).collect(
                Collectors.toList());
    }

}
