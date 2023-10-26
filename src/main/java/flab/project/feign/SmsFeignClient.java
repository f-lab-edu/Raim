package flab.project.feign;

import feign.HeaderMap;
import feign.Headers;
import feign.Param;
import flab.project.dto.SmsRequestDto;
import flab.project.dto.SmsResponseDto;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "NaverCloudSmsOpenFeign", url = "https://sens.apigw.ntruss.com")
public interface SmsFeignClient {

    @PostMapping("/sms/v2/services/{serviceId}/messages")
    @Headers("Content-Type: application/json")
    ResponseEntity<SmsResponseDto> sendSms(@PathVariable String serviceId,
                                           @RequestHeader("x-ncp-apigw-timestamp") String time,
                                           @RequestHeader("x-ncp-iam-access-key") String accessKey,
                                           @RequestHeader("x-ncp-apigw-signature-v2") String signature,
                                           @RequestBody SmsRequestDto json);
}
