package flab.project.service;

import flab.project.domain.SmsVerification;
import flab.project.dto.SmsRequestDto;
import flab.project.dto.SmsResponseDto;
import flab.project.exception.ExceptionCode;
import flab.project.exception.KakaoException;
import flab.project.feign.SmsFeignClient;
import flab.project.repository.SmsVerificationRepository;
import flab.project.util.EncryptionUtils;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@PropertySource("classpath:application.properties")
@RequiredArgsConstructor
@Service
public class SmsService {

    @Value("${naver-cloud-sms.accessKey}")
    private String accessKey;

    @Value("${naver-cloud-sms.secretKey}")
    private String secretKey;

    @Value("${naver-cloud-sms.serviceId}")
    private String serviceId;

    @Value("${naver-cloud-sms.senderPhone}")
    private String from;

    private final SmsVerificationRepository smsVerificationRepository;
    private final SmsFeignClient smsFeignClient;

    public void sendAuthenticationSms(String phoneNumber) {
        long milliseconds = System.currentTimeMillis();

        String verificationCode = makeRandomMessage();
        SmsRequestDto sms = makeSms(phoneNumber, verificationCode);
        sendSmsApi(Long.toString(milliseconds), sms);

        if (smsVerificationRepository.existsByPhoneNumber(phoneNumber)) {
            updatePhoneNumberVerification(milliseconds, phoneNumber, verificationCode);
        } else {
            createPhoneNumberVerification(milliseconds, phoneNumber, verificationCode);
        }

    }

    private SmsVerification updatePhoneNumberVerification(long milliseconds, String phoneNumber, String verificationCode) {
        SmsVerification findSmsVerification = smsVerificationRepository.findByPhoneNumber(phoneNumber)
                .orElseGet(() -> SmsVerification.builder().build());

        LocalDateTime createdAt = Instant.ofEpochMilli(milliseconds).atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime expirationTime = Instant.ofEpochMilli((milliseconds + 300000)).atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        findSmsVerification.renewSmsVerification(verificationCode, createdAt, expirationTime);

        return smsVerificationRepository.save(findSmsVerification);
    }

    private SmsVerification createPhoneNumberVerification(long milliseconds, String phoneNumber, String verificationCode) {
        LocalDateTime createdAt = Instant.ofEpochMilli(milliseconds).atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime expirationTime = Instant.ofEpochMilli((milliseconds + 300000)).atZone(ZoneId.systemDefault())
                .toLocalDateTime();



        SmsVerification smsVerification = SmsVerification.builder()
                .phoneNumber(phoneNumber)
                .smsEncryptKey(EncryptionUtils.generateEncryptedKey(phoneNumber))
                .verificationCode(verificationCode)
                .expirationTime(expirationTime)
                .isVerified(false)
                .createdAt(createdAt)
                .build();

        return smsVerificationRepository.save(smsVerification);
    }

    private SmsRequestDto makeSms(String phoneNumber, String verificationCode) {
        List<SmsRequestDto.MessageDto> messages = new ArrayList<>();
        messages.add(SmsRequestDto.MessageDto.builder().to(phoneNumber).build());

        return SmsRequestDto.builder()
                .type("SMS")
                .contentType("COMM")
                .countryCode("82")
                .from(from)
                .content("sms 본인인증 번호는 [" + verificationCode + "] 입니다.")
                .messages(messages)
                .build();
    }

    private void sendSmsApi(String time, SmsRequestDto smsRequestDto) {

        /* sendSms의 ErrorResponse 처리하는 법 */
        ResponseEntity<SmsResponseDto> response = smsFeignClient.sendSms(serviceId, time, accessKey, makeSignature(time), smsRequestDto);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new KakaoException(ExceptionCode.API_FAIL);
        }

    }

    public String makeSignature(String time) {
        String space = " ";
        String newLine = "\n";
        String method = "POST";
        String url = "/sms/v2/services/" + this.serviceId + "/messages";
        String timestamp = time;
        String accessKey = this.accessKey;
        String secretKey = this.secretKey;

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey = null;
        Mac mac = null;
        try {
            signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
            mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new KakaoException(ExceptionCode.API_FAIL);
        }

        byte[] rawHmac = new byte[0];
        try {
            rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new KakaoException(ExceptionCode.API_FAIL);
        }

        String encodeBase64String = Base64.encodeBase64String(rawHmac);

        return encodeBase64String;
    }

    public String makeRandomMessage() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }

    @Transactional
    public String checkSmsCode(String phoneNumber, String code) {

        SmsVerification checkedSmsVerification = smsVerificationRepository.findByPhoneNumber(phoneNumber)
                .filter(findVerification -> {
                    if (!LocalDateTime.now().isBefore(findVerification.getExpirationTime())) {
                        throw new KakaoException(ExceptionCode.SMS_EXPIRED_VERIFICATION);
                    }

                    if (!findVerification.getVerificationCode().equals(code)) {
                        throw new KakaoException(ExceptionCode.SMS_CODE_NOT_MATCH);
                    }

                    findVerification.successVerification();
                    smsVerificationRepository.save(findVerification);

                    return true;
                }).orElseThrow(() -> new KakaoException(ExceptionCode.SMS_VERIFICATION_NOT_FOUND));

        return checkedSmsVerification.getSmsEncryptKey();
    }
}
