package flab.project.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String emailEncryptKey;

    @Column(nullable = false)
    private boolean isVerified;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expirationTime;

    @Builder
    public EmailVerification(String email, String emailEncryptKey, boolean isVerified, LocalDateTime createdAt, LocalDateTime expirationTime) {
        this.email = email;
        this.emailEncryptKey = emailEncryptKey;
        this.isVerified = isVerified;
        this.createdAt = createdAt;
        this.expirationTime = expirationTime;
    }

    public void renewEmailVerification(LocalDateTime createdAt, LocalDateTime expirationTime) {
        this.createdAt = createdAt;
        this.expirationTime = expirationTime;
    }

    public boolean checkExpiration(LocalDateTime now) {
        if (!now.isBefore(this.expirationTime)) {
            return false;
        }

        return true;
    }

    public boolean isVerified() {
        return isVerified;
    }
}
