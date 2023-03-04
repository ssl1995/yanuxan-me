package com.msb.user.visitor.handler;

import com.msb.framework.redis.RedisClient;
import com.msb.user.auth.AuthAdmin;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

/**
 * @author 86151
 */
@Slf4j
@Component
@RefreshScope
public class VisitorHandlerInterceptor implements HandlerInterceptor {

    public static final String VISITOR_KEY = "horse-user:user_visitor:";

    public static final String VISITOR_UV_UUID = "visitor_uuid_uv";

    public static final String VISITOR_PV_UUID = "visitor_uuid_pv";

    public static final String HEADER_UUID = "uid";

    @Resource
    private RedisClient redisClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            log.info("统计uv");
            statisticsUv(request, handler);
            log.info("统计pv");
            statisticsPv(handler);
        } catch (Exception e) {
            log.error("统计uv，pv错误,", e);
        }
        return true;
    }

    public void statisticsUv(HttpServletRequest request, Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        AuthAdmin methodAuthAdmin = handlerMethod.getMethod().getAnnotation(AuthAdmin.class);
        AuthAdmin beanAuthAdmin = handlerMethod.getBeanType().getAnnotation(AuthAdmin.class);
        if (methodAuthAdmin != null || beanAuthAdmin != null) {
            // 方法或者bean上有这个注解 说明不需要统计访客
            log.info("该接口有@AuthAdmin注解，不需要统计uv访客记录");
            return;
        }

        String uuid = request.getHeader(HEADER_UUID);
        if (StringUtils.isBlank(uuid)) {
            log.warn("uuid为空：" + uuid + "-----------------------------");
            return;
        }

        log.info("uuid:" + uuid + "-----------------------------");
        // 保存到Redis中
        LocalDate now = LocalDate.now();
        redisClient.pfAdd(VISITOR_KEY + VISITOR_UV_UUID + ":" + now, uuid);
    }

    public void statisticsPv(Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        AuthAdmin methodAuthAdmin = handlerMethod.getMethod().getAnnotation(AuthAdmin.class);
        AuthAdmin beanAuthAdmin = handlerMethod.getBeanType().getAnnotation(AuthAdmin.class);
        if (methodAuthAdmin != null || beanAuthAdmin != null) {
            // 方法或者bean上有这个注解 说明不需要统计访客
            log.info("该接口有@AuthAdmin注解，不需要统计pv访客记录");
            return;
        }
        String key = VISITOR_KEY + VISITOR_PV_UUID + ":" + LocalDate.now();
        redisClient.increment(key, 1L);
    }
}
