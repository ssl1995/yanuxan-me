package com.msb.user.auth;


import com.msb.framework.common.context.UserContext;
import com.msb.framework.common.model.UserLoginInfo;
import com.msb.user.api.UserDubboService;
import com.msb.user.auth.exception.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;

/**
 * 鉴权Token的拦截
 * @author liao
 */
@Slf4j
@Component
public class AuthHandlerInterceptor implements HandlerInterceptor {

    @Resource
    private JwtProperties jwtProperties;

    @DubboReference(timeout = 10000)
    private UserDubboService userDubboService;

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HandlerMethod handlerMethod;
        AuthAdmin authAdmin = null;
        String jwtToken = request.getHeader(JwtConstants.JWT_HEADER_KEY);

        if (handler instanceof HandlerMethod) {
            handlerMethod = (HandlerMethod) handler;
            if (handlerMethod.getMethod().isAnnotationPresent(NoAuth.class)
                    || handlerMethod.getBeanType().isAnnotationPresent(NoAuth.class)) {
                if (StringUtils.isNotBlank(jwtToken)) {
                    UserLoginInfo userLoginInfo = userDubboService.checkUserLoginInfo(jwtToken);
                    if (Objects.nonNull(userLoginInfo)) {
                        setUserContext(userLoginInfo);
                    }
                }
                return true;
            }
            authAdmin = Optional.ofNullable(handlerMethod.getBeanType().getAnnotation(AuthAdmin.class))
                    .orElse(handlerMethod.getMethod().getAnnotation(AuthAdmin.class));
        }

        if (StringUtils.isBlank(jwtToken)) {
            log.error("请求地址 {} 没有token", request.getRequestURI());
            throw new AuthException();
        }

        Map<String, String> requestMap = new TreeMap<>();
        requestMap.put("uri", request.getRequestURI());
        requestMap.put("method", request.getMethod());
        requestMap.put("service", applicationName);

        UserLoginInfo userLoginInfo = Optional.ofNullable(authAdmin)
                .map(authAdmin1 -> userDubboService.checkEmployeePermission(jwtToken, requestMap))
                .orElseGet(() -> userDubboService.checkUserLoginInfo(jwtToken));

        log.info("拦截器解析token {} userLoginInfo {}", jwtToken, userLoginInfo);

        if (userLoginInfo == null) {
            throw new AuthException();
        }

        setUserContext(userLoginInfo);
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.remove();
    }

    private void setUserContext(UserLoginInfo userLoginInfo) {
        UserContext.set(userLoginInfo);
    }
}
