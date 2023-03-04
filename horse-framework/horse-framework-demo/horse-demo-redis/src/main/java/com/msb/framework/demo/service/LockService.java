package com.msb.framework.demo.service;


import com.msb.framework.demo.Product;
import com.msb.framework.redis.lock.Lock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Lock service
 *
 * @author R
 */
@Slf4j
@Service
public class LockService {


    @Lock("#key")
    public int lock(String key) throws InterruptedException {
        Thread.sleep(3000);
        return 1;
    }

    /**
     * 锁两个参数
     *
     * @param key  参数1
     * @param key2 参数2
     */
    @Lock({"#key", "#key2"})
    public int lock2(String key, String key2) throws InterruptedException {
        Thread.sleep(3000);
        return 1;
    }

    /**
     * 锁入参对象的属性，此处演示锁商品的id
     * 并展示设置等待时间和自定义超时异常信息功能
     *
     * @param product 商品
     */
    @Lock(key = "#product.id", waitTime = 3, errorMsg = "'等待超过'+#waitTime+'秒，系统繁忙，请稍后再试。'")
    public int lockObject(Product product) throws InterruptedException {
        Thread.sleep(5000);
        return 0;
    }
}
