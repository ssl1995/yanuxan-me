package com.msb.framework.demo;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.Map;

@SpringBootTest
public class RedisTest {
    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testRedis() {
        Map map = JSON.parseObject("{\"startTime\":\"2022-10-17 20:24:51\",\"type\":\"1\",\"userId\":\"D9A0CF224BF92F5B\",\"liveId\":\"A76D82F905FF5FBE9C33DC5901307461\",\"roomId\":\"071207990359270C9C33DC5901307461\"}", Map.class);
//        "{\"@class\":\"java.util.HashMap\",\"startTime\":\"2022-10-17 19:52:50\",\"endTime\":\"2022-10-17 20:01:26\",\"stopStatus\":\"10\",\"type\":\"2\",\"userId\":\"D9A0CF224BF92F5B\",\"liveId\":\"8B6A6276581089409C33DC5901307461\",\"roomId\":\"9E1C0D5EE6B59A249C33DC5901307461\"}"
        redisTemplate.opsForHash().put("test111111", "111", map);
        Map test = redisTemplate.opsForHash().entries("test111111");

        System.out.println(test);
    }
}
