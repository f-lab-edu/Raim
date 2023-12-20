package flab.project.config;

import flab.project.feign.AddressFeignClient;
import flab.project.util.AddressUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WebConfig {

    private final AddressFeignClient addressFeignClient;

    @Bean
    public AddressUtils addressUtils() {
        return new AddressUtils(addressFeignClient.getMyExternalIP());
    }
}
