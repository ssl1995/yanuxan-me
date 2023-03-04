package com.msb.im.redis;

import com.msb.framework.redis.RedisClient;
import com.msb.im.model.entity.ThirdSystemConfig;
import com.msb.im.service.ThirdSystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.msb.im.redis.RedisConstant.*;

/**
 * 消息相关的redis操作服务
 *
 * @author zhoumioa
 * @date 2022-04-23
 */
@Service
public class SystemRedisService {

    @Autowired
    private RedisClient redisClient;
    @Resource
    private ThirdSystemConfigService thirdSystemConfigService;

    public void deleteThirdSystem(int sysId) {
        String thirdSystemKey = getThirdSystemKey(sysId);
        redisClient.delete(thirdSystemKey);
    }

    public void addThirdSystem(ThirdSystemConfig thirdSystemConfig) {
        if (thirdSystemConfig == null) {
            return;
        }
        String thirdSystemKey = getThirdSystemKey(thirdSystemConfig.getId());
        redisClient.set(thirdSystemKey, thirdSystemConfig, THIRD_SYSTEM_EXPIRE_DAYS, TimeUnit.DAYS);
    }

    public ThirdSystemConfig getThirdSystem(int sysId) {
        String thirdSystemKey = getThirdSystemKey(sysId);
        return redisClient.get(thirdSystemKey);
    }

    public ThirdSystemConfig getThirdSystem(String client) {
        String thirdSystemKey = getThirdSystemKey(client);
        return redisClient.get(thirdSystemKey);
    }


    public void loadThirdSystemConfig2Cache() {
        List<ThirdSystemConfig> thirdSystemConfigs = thirdSystemConfigService.list();
        for (ThirdSystemConfig thirdSystemConfig : thirdSystemConfigs) {
            addThirdSystem(thirdSystemConfig);
        }
    }

    public String getThirdSystemKey(int sysId) {
        return RedisConstant.format(THIRD_SYSTEM_KEY, sysId);
    }

    public String getThirdSystemKey(String client) {
        return RedisConstant.format(THIRD_SYSTEM_KEY, client);
    }

}
