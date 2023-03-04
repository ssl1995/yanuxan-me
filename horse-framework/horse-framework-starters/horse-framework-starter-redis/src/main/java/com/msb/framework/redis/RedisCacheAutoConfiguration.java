package com.msb.framework.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.msb.framework.redis.annotation.ExpireRedisCacheWriter;
import com.msb.framework.redis.aspect.CacheExpireAspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * RedisCache相关配置
 *
 * @author luozhan
 * @date 2022-3-18
 */
@EnableCaching
@Configuration
@Import({RedisClient.class, CacheExpireAspect.class})
public class RedisCacheAutoConfiguration extends CachingConfigurerSupport {
    @Value("${spring.application.name:unknown}")
    private String appName;


    /**
     * 注入RedisTemplate
     * tips：泛型可以适用于各种类型的注入
     */
    @Bean
    public <T> RedisTemplate<String, T> redisTemplate(RedisConnectionFactory factory, RedisSerializer<?> redisSerializer) {
        // 指定序列化方式
        RedisTemplate<String, T> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(redisSerializer);
        redisTemplate.setHashValueSerializer(redisSerializer);
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }

    /**
     * 注入RedisCacheManager
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory, RedisSerializer<Object> redisSerializer) {

        RedisCacheConfiguration config = RedisCacheConfiguration
                // 注意默认配置允许缓存null
                .defaultCacheConfig()
                // 接口缓存上指定了key的时候统一加服务名前缀
                .computePrefixWith(cacheName -> appName + ":" + cacheName + ":")
                // 可以根据业务需要设置统一过期时间，这里是为了强制使用@CacheExpire手动设置过期时间所以设置很短
                .entryTtl(Duration.ofMinutes(1))
                // 配置序列化和反序列化方式
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer));
        return RedisCacheManager.builder(new ExpireRedisCacheWriter(factory)).cacheDefaults(config).build();
    }

    /**
     * 使用@Cachable生成的key的默认规则
     * 如果手动指定了key则不生效
     * <p>
     * 格式：入参1,入参2
     * 示例：1,2
     * 加上前缀后完整key格式：order_service:order:1,2
     * <p>
     * 建议使用@Cacheable的时候都指定key
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> Stream.of(params).map(String::valueOf).collect(Collectors.joining(","));
    }

    /**
     * 初始化Redis序列器，使用jackson
     */
    @Bean
    public RedisSerializer<Object> redisSerializer() {
        ObjectMapper objectMapper = new ObjectMapper();
        // jdk8日期格式支持
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Module timeModule = new JavaTimeModule()
                .addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter))
                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(timeFormatter))
                .addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter))
                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(timeFormatter));
        //兼容学习平台
        objectMapper.registerModule(timeModule);
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        return jackson2JsonRedisSerializer;
    }

}
