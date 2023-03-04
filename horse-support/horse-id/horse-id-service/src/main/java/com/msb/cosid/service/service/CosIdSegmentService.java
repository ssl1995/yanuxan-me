package com.msb.cosid.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.cosid.service.mapper.CosIdSegmentMapper;
import com.msb.cosid.service.model.entity.CosIdSegment;
import com.msb.framework.redis.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.msb.cosid.service.model.constant.RedisKeysConstants.INIT_BUSINESS_COS_ID_LOCK;

/**
 * @author liao
 */
@Slf4j
@Service
public class CosIdSegmentService extends ServiceImpl<CosIdSegmentMapper, CosIdSegment> {

    @Resource
    private RedisClient redisClient;

    public CosIdSegment saveOrUpdate(String businessId) {
        CosIdSegment cosIdSegment = this.lambdaQuery().eq(CosIdSegment::getBusinessId, businessId).one();
        if (cosIdSegment != null) {
            return cosIdSegment;
        }
        RLock lock = redisClient.getLock(INIT_BUSINESS_COS_ID_LOCK.concat(businessId));
        try {
            lock.tryLock(20, 20, TimeUnit.MINUTES);
            cosIdSegment = this.lambdaQuery().eq(CosIdSegment::getBusinessId, businessId).one();
            if (Objects.isNull(cosIdSegment)) {
                cosIdSegment = new CosIdSegment()
                        .setBusinessId(businessId)
                        .setAutoIncrement(1L);
                this.save(cosIdSegment);
            }
            return cosIdSegment;
        } catch (Exception e) {
            log.error("获取号段失败", e);
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
        return null;
    }


}
