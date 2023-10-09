package flab.project.service;

import flab.project.domain.EmailVerification;
import flab.project.domain.SmsVerification;
import flab.project.domain.User;
import flab.project.repository.EmailVerificationRepository;
import flab.project.repository.SmsVerificationRepository;
import flab.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final SmsVerificationRepository smsVerificationRepository;

    public boolean registrationUser(User user) {
        if (isRegistered(user)) {
            return false;
        }

        LocalDateTime now = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.systemDefault()).toLocalDateTime();

        if (!checkEmailVerification(user.getEmail(), now) || !checkSmsVerification(user.getPhoneNumber(), now)) {
            return false;
        }

        userRepository.save(user);

        return true;
    }

    private boolean checkEmailVerification(String email, LocalDateTime now) {
        EmailVerification findEmailVerification = emailVerificationRepository.findByEmail(email)
                .orElseThrow(() ->  new NoSuchElementException());

        if (!findEmailVerification.checkExpiration(now) || !findEmailVerification.isVerified()) {
            return false;
        }

        return true;
    }

    private boolean checkSmsVerification(String phoneNumber, LocalDateTime now) {
        SmsVerification findSmsVerification = smsVerificationRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new NoSuchElementException());

        if (!findSmsVerification.checkExpiration(now) || !findSmsVerification.isVerified()) {
            return false;
        }

        return true;
    }

    public boolean duplicatedEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            return true;
        }

        if (emailVerificationRepository.existsByEmail(email)) {
            updateEmailVerification(email);
        } else {
            createEmailVerification(email);
        }

        return false;
    }

    private void updateEmailVerification(String email) {
        EmailVerification findEmail = emailVerificationRepository.findByEmail(email).get();

        Long milliseconds = System.currentTimeMillis();
        LocalDateTime createdAt = Instant.ofEpochMilli(milliseconds).atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime expirationTime = Instant.ofEpochMilli((milliseconds + 300000)).atZone(ZoneId.systemDefault()).toLocalDateTime();

        findEmail.renewEmailVerification(createdAt, expirationTime);

        emailVerificationRepository.save(findEmail);
    }

    private void createEmailVerification(String email) {
        Long milliseconds = System.currentTimeMillis();
        LocalDateTime createdAt = Instant.ofEpochMilli(milliseconds).atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime expirationTime = Instant.ofEpochMilli((milliseconds + 300000)).atZone(ZoneId.systemDefault()).toLocalDateTime();

        EmailVerification emailVerification = EmailVerification.builder()
                .email(email)
                .isVerified(true)
                .createdAt(createdAt)
                .expirationTime(expirationTime)
                .build();

        emailVerificationRepository.save(emailVerification);
    }

    private boolean isRegistered(User user) {
        return userRepository.existsByEmail(user.getEmail());
    }
}
