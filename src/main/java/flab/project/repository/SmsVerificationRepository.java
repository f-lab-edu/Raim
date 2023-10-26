package flab.project.repository;

import flab.project.domain.SmsVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface SmsVerificationRepository extends JpaRepository<SmsVerification, Long> {

    Optional<SmsVerification> findByPhoneNumber(String phoneNumber);
    void deleteByExpirationTimeBefore(LocalDateTime referenceTime);
    void deleteByPhoneNumber(String phoneNumber);
}
