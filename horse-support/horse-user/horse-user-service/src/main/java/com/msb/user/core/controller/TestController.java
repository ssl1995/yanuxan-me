package com.msb.user.core.controller;

import com.msb.user.auth.NoAuth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Semaphore;

@NoAuth
@Slf4j
@RestController
public class TestController {

    private final Object lock = new Object();


    Semaphore semp = new Semaphore(1);

    @GetMapping("test")
    public Integer test(Integer i) throws InterruptedException {
//        new User().setId(1L)

        semp.acquire();

        //todo

        semp.release();

        if (i == 1) {
            synchronized (lock) {
                log.info("进来等待 i {}", i);
                lock.wait();
                log.info("notify 执行了 i {} ", i);
            }
        } else {
            synchronized (lock) {
                lock.notifyAll();
            }
        }
        return i;
    }
}
