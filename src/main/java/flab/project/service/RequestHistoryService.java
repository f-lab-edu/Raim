package flab.project.service;

import flab.project.domain.RequestHistory;
import flab.project.exception.ExceptionCode;
import flab.project.exception.KakaoException;
import flab.project.repository.RequestHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RequestHistoryService {

    private final long COOL_DOWN_TIME = 5;

    private final RequestHistoryRepository requestHistoryRepository;

    public void checkAccessRequest(String sessionId) {
        RequestHistory history = null;
        if (!requestHistoryRepository.existsBySessionId(sessionId)) {
            history = RequestHistory.builder()
                    .sessionId(sessionId)
                    .requestTime(LocalDateTime.now())
                    .build();

        } else {
            history = requestHistoryRepository.findBySessionId(sessionId).get();

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime lastRequestTime = history.getRequestTime();
            if (now.isBefore(lastRequestTime.plusSeconds(COOL_DOWN_TIME))) {
                throw new KakaoException(ExceptionCode.REQUEST_TOO_FAST);
            }

            history.updateRequestTime(now);
        }

        requestHistoryRepository.save(history);
    }
}
