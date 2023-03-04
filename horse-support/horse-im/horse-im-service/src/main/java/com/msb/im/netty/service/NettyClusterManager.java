package com.msb.im.netty.service;

import com.msb.framework.redis.RedisClient;
import com.msb.im.redis.RedisConstant;
import com.msb.im.redis.RedisService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * netty集群管理
 *
 * @author zhou miao
 * @date 2022/05/25
 */
@Service
public class NettyClusterManager {

    @Resource
    private RedisService redisService;
    @Resource
    private RedisClient redisClient;

    public boolean isClusterMember(String ip) {
        return redisService.containsNettyServer(ip);
    }

    public void increment(Integer systemId) {
        String key = getSysOnlineUserCountKey(systemId);
        redisClient.increment(key);
    }

    public void decrement(Integer systemId) {
        String key = getSysOnlineUserCountKey(systemId);
        redisClient.decrement(key);
    }

    public Integer getSysOnlineUserCount(Integer systemId) {
        String key = getSysOnlineUserCountKey(systemId);
        Integer userCount = redisClient.get(key);
        return userCount == null ? 0 : userCount;
    }

    private String getSysOnlineUserCountKey(Integer systemId) {
        return RedisConstant.format(RedisConstant.SYS_ONLINE_USER_COUNT_KEY, systemId);
    }


}
