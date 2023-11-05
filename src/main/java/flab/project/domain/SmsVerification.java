package flab.project.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class SmsVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String smsEncryptKey;

    @Column(nullable = false)
    private String verificationCode;

    @Column(nullable = false)
    private LocalDateTime expirationTime;

    @Column(nullable = false)
    private boolean isVerified;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public void successVerification() {
        this.isVerified = true;
    }

    @Builder
    public SmsVerification(String phoneNumber, String smsEncryptKey, String verificationCode, LocalDateTime expirationTime, boolean isVerified, LocalDateTime createdAt) {
        this.phoneNumber = phoneNumber;
        this.smsEncryptKey = smsEncryptKey;
        this.verificationCode = verificationCode;
        this.expirationTime = expirationTime;
        this.isVerified = isVerified;
        this.createdAt = createdAt;
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

    public void renewSmsVerification(String verificationCode, LocalDateTime createdAt, LocalDateTime expirationTime) {
        this.verificationCode = verificationCode;
        this.createdAt = createdAt;
        this.expirationTime = expirationTime;
    }
}
