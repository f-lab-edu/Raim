package flab.project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import flab.project.domain.SmsVerification;
import flab.project.dto.SmsRequestDto;
import flab.project.dto.SmsResponseDto;
import flab.project.repository.SmsVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

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

    public HttpStatusCode sendAuthenticationSms(String phoneNumber) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        long milliseconds = System.currentTimeMillis();

        //sms 확인을 위한 엔티티 생성 및 db에 저장
        LocalDateTime createdAt = Instant.ofEpochMilli(milliseconds).atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime expirationTime = Instant.ofEpochMilli((milliseconds + 300000)).atZone(ZoneId.systemDefault()).toLocalDateTime();

        String verificationCode = makeRandomMessage();

        SmsVerification smsVerification = SmsVerification.builder()
                .phoneNumber(phoneNumber)
                .verificationCode(verificationCode)
                .expirationTime(expirationTime)
                .isVerified(false)
                .createdAt(createdAt)
                .build();

        smsVerificationRepository.save(smsVerification);

        //api 요청에 맞는 json 생성
        HttpEntity<String> json = getRequestJson(Long.toString(milliseconds), phoneNumber, verificationCode);

        //api 요청
        ResponseEntity<SmsResponseDto> response = sendSmsApi(json);

        return response.getStatusCode();
    }

    private ResponseEntity<SmsResponseDto> sendSmsApi(HttpEntity<String> json) {
        RestTemplate restTemplate = new RestTemplate();

        String uri = "https://sens.apigw.ntruss.com/sms/v2/services/" + serviceId + "/messages";
        ResponseEntity<SmsResponseDto> response = restTemplate.postForEntity(uri, json, SmsResponseDto.class);
        return response;
    }

    private HttpEntity<String> getRequestJson(String time, String phoneNumber, String verificationCode) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time);
        headers.set("x-ncp-iam-access-key", accessKey);
        headers.set("x-ncp-apigw-signature-v2", makeSignature(time));

        List<SmsRequestDto.MessageDto> messages = new ArrayList<>();
        messages.add(SmsRequestDto.MessageDto.builder().to(phoneNumber).build());

        SmsRequestDto sms = SmsRequestDto.builder()
                .type("SMS")
                .contentType("COMM")
                .countryCode("82")
                .from(from)
                .content("sms 본인인증 번호는 [" + verificationCode + "] 입니다.")
                .messages(messages)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(sms);

        return new HttpEntity<>(body, headers);
    }
    public String makeSignature(String time) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
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

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
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
    public boolean checkSmsCode(String phoneNumber, String code) {

        SmsVerification result = smsVerificationRepository.findByPhoneNumber(phoneNumber).orElseThrow(() -> new NoSuchElementException());

        //기간이 만료되었는지 확인
        if (!LocalDateTime.now().isBefore(result.getExpirationTime())) {
            throw new IllegalArgumentException();
        }

        //코드가 맞는지 확인
        if (!result.getVerificationCode().equals(code)) {
            return false;
        }

        result.successVerification();
        smsVerificationRepository.save(result);

        return true;
    }
}
