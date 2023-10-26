package flab.project.repository;

import flab.project.domain.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {

    boolean existsByEmail(String email);
    Optional<EmailVerification> findByEmail(String email);
    void deleteByExpirationTimeBefore(LocalDateTime referenceTime);
    void deleteByEmail(String email);
}
