package flab.project.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "MyExternalIPOpenFeign", url = "http://checkip.amazonaws.com")
public interface AddressFeignClient {

    @GetMapping
    String getMyExternalIP();
}
