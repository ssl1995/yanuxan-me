package com.msb.framework.redis.lock;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 * 默认使用Redis锁
 *
 * @author luozhan
 * @date 2020-07
 * @see LockAspect
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Lock {

    /**
     * 指定锁的key，使用spEL表达式
     * <p>
     * 若不指定key，锁的粒度为"方法"，等同于分布式的synchronized，即表示同一时间只有一个线程能进入该方法
     * 如果指定key，锁的粒度为"方法+指定key的表达式解析值"
     * 最后写入redis中的key格式为："lock:@类名.方法名"、"lock:@类名.方法名(指定参数1,指定参数2,...)"
     * <p>
     * 示例：
     * 1.使用入参作为key： key="#productId"
     * 2.入参是对象时，想以对象中的属性作为key： key="#product.id"
     * 3.多个入参联合作为key： key={"#productId", "#userId"}
     */
    @AliasFor("value")
    String[] key() default {};

    /**
     * 加锁后自动释放时间
     * 默认自动续期（-1）
     */
    int leaseTime() default -1;

    /**
     * 最大等待时长，超过后还未加锁成功则抛出异常
     * 默认单位：秒，设置0表示不等待，建议配合自定义异常信息使用
     */
    int waitTime() default 2;

    /**
     * 超时后抛出异常的错误信息
     * 支持spEL，允许的参数有方法的入参以及 #waitTime
     */
    String errorMsg() default "${spring.lock.errorMsg}";


    /**
     * 时间单位
     */
    TimeUnit unit() default TimeUnit.SECONDS;

    @AliasFor("key")
    String[] value() default {};


}
