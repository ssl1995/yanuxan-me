package com.msb.user.auth;


import com.msb.user.api.constant.SortConstant;
import com.msb.user.auth.exception.AuthExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author liao
 */
@Configuration
@Import({JwtProperties.class, NoAuthConfig.class, AuthHandlerInterceptor.class, DevAuthHandlerInterceptor.class, AuthExceptionHandler.class})
public class AuthInterceptorConfig implements WebMvcConfigurer {

    private static final String[] NO_AUTH = {"/**/api-docs", "/**.html", "/swagger-resources/**", "/v3/**", "/webjars/**", "/favicon.ico", "/"};

    @Resource
    private NoAuthConfig noAuthConfig;

    @Resource
    private AuthHandlerInterceptor authHandlerInterceptor;

    @Resource
    private DevAuthHandlerInterceptor devAuthHandlerInterceptor;

    @Resource
    private Environment environment;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration = null;
        interceptorRegistration = registry.addInterceptor(authHandlerInterceptor);
//        String[] activeProfiles = environment.getActiveProfiles();
//        if (Objects.equals(activeProfiles[0], "dev")) {
//            interceptorRegistration = registry.addInterceptor(devAuthHandlerInterceptor);
//        } else {
//        }
        //默认不拦截swagger，加载业务侧 不拦截配置
        List<String> noAuth = new ArrayList<>(Arrays.asList(NO_AUTH));
        if (noAuthConfig.getWhiteList() != null && !noAuthConfig.getWhiteList().isEmpty()) {
            noAuth.addAll(noAuthConfig.getWhiteList());
        }
        interceptorRegistration.addPathPatterns("/**").excludePathPatterns(noAuth).order(SortConstant.TWO);
    }
}
