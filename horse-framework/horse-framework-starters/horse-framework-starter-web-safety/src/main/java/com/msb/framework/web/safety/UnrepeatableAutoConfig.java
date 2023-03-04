package com.msb.framework.web.safety;

import com.msb.framework.redis.RedisClient;
import com.msb.framework.web.safety.repeat.UnRepeatableRequestAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author liao
 */
@Configuration
@ConditionalOnBean(RedisClient.class)
@Import({UnRepeatableRequestAspect.class})
public class UnrepeatableAutoConfig {
}
