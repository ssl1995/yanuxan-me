package com.msb.user.visitor.handler;

import com.msb.user.api.constant.SortConstant;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author 86151
 */
@Configuration
@Import(VisitorHandlerInterceptor.class)
public class VisitorInterceptorConfig implements WebMvcConfigurer {
    @Resource
    private VisitorHandlerInterceptor visitorHandlerInterceptor;
    private static final String[] NO_HANDLER = {"/**/api-docs", "/**.html", "/swagger-resources/**", "/v3/**", "/webjars/**", "/favicon.ico"};

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截的路径
        String[] addPathPatterns = {"/**"};
        registry.addInterceptor(visitorHandlerInterceptor).addPathPatterns(addPathPatterns).excludePathPatterns(NO_HANDLER).order(SortConstant.ONE);
    }
}
