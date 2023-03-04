package com.msb.framework.demo.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(url = "127.0.0.1:8080", name = "ffff", path = "/")
public interface DemoFeignClient {
    @PostMapping("/feign")
    String feignMethod();
}
