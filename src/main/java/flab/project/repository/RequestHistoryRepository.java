package flab.project.repository;

import flab.project.domain.RequestHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RequestHistoryRepository extends JpaRepository<RequestHistory, Long> {
    boolean existsBySessionId(String sessionId);
    Optional<RequestHistory> findBySessionId(String sessionId);
}
