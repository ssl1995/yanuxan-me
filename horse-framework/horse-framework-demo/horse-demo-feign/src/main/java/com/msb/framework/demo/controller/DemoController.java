package com.msb.framework.demo.controller;


import com.msb.framework.common.exception.BizException;
import com.msb.framework.demo.feign.DemoFeignClient;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@Api
public class DemoController implements DemoFeignClient {

    @Resource
    DemoFeignClient demoFeignClient;

    @GetMapping("/test")
    public String test() {

        String s = demoFeignClient.feignMethod();
        return s;
    }

    public String feignMethod() {
        if (true) {
            throw new BizException("1123");
        }
        return "this is feign method!";
    }
}
