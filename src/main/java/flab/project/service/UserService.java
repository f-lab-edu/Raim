package flab.project.service;

import flab.project.domain.EmailVerification;
import flab.project.domain.SmsVerification;
import flab.project.domain.User;
import flab.project.exception.*;
import flab.project.repository.EmailVerificationRepository;
import flab.project.repository.SmsVerificationRepository;
import flab.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RequestHistoryService requestHistoryService;
    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final SmsVerificationRepository smsVerificationRepository;

    @Transactional
    public void registrationUser(User user) {
        if (isRegistered(user)) {
            throw new UserExistsException(ExceptionCode.USER_EXIST);
        }

        LocalDateTime now = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.systemDefault()).toLocalDateTime();

        checkEmailVerification(user.getEmail(), now);
        checkSmsVerification(user.getPhoneNumber(), now);

        userRepository.save(user);

        cleanVerification(user);
    }

    private void cleanVerification(User user) {
        emailVerificationRepository.deleteByEmail(user.getEmail());
        smsVerificationRepository.deleteByPhoneNumber(user.getPhoneNumber());
    }

    public String findEmail(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).map(findUser -> findUser.getEmail())
                .orElseThrow(() -> new EmailNotFoundException(ExceptionCode.EMAIL_NOT_FOUND));
    }

    private void checkEmailVerification(String email, LocalDateTime now) {
        EmailVerification findVerification = emailVerificationRepository.findByEmail(email)
                .orElseThrow(() -> new VerificationException(ExceptionCode.EMAIL_VERIFICATION_NOT_FOUND));

        if (!findVerification.checkExpiration(now)) {
            throw new VerificationException(ExceptionCode.EMAIL_EXPIRED_VERIFICATION);
        }

        if (!findVerification.isVerified()) {
            throw new VerificationException(ExceptionCode.EMAIL_UNVERIFIED_VERIFICATION);
        }
    }

    private void checkSmsVerification(String phoneNumber, LocalDateTime now) {
        SmsVerification findVerification = smsVerificationRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new VerificationException(ExceptionCode.SMS_VERIFICATION_NOT_FOUND));

        if (!findVerification.checkExpiration(now)) {
            throw new VerificationException(ExceptionCode.SMS_EXPIRED_VERIFICATION);
        }

        if (!findVerification.isVerified()) {
            throw new VerificationException(ExceptionCode.SMS_UNVERIFIED_VERIFICATION);
        }
    }

    public void duplicatedEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException(ExceptionCode.DUPLICATED_EMAIL);
        }

        if (emailVerificationRepository.existsByEmail(email)) {
            updateEmailVerification(email);
        } else {
            createEmailVerification(email);
        }
    }

    private void updateEmailVerification(String email) {
        EmailVerification findEmail = emailVerificationRepository.findByEmail(email).orElseGet(() -> EmailVerification.builder().build());

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
