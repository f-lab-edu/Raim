package flab.project.config;

import flab.project.interceptor.SmsRequestInterceptor;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("flab.project.feign")
public class OpenFeignConfig {

    @Bean
    public SmsRequestInterceptor smsRequestInterceptor() {
        return new SmsRequestInterceptor();
    }

    @Bean
    feign.Logger.Level feignLoggerLevel() {
        return feign.Logger.Level.FULL;
    }
}
