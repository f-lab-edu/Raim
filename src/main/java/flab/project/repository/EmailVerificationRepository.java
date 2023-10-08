package flab.project.repository;

import flab.project.domain.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {

    boolean existsByEmail(String email);
    Optional<EmailVerification> findByEmail(String email);
}
