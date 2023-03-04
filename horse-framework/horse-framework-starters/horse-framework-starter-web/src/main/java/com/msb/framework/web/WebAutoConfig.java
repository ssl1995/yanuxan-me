package com.msb.framework.web;

import com.msb.framework.web.config.DateTimeConfig;
import com.msb.framework.web.config.DateTimeConvertPrimaryConfig;
import com.msb.framework.web.exception.GlobalExceptionHandler;
import com.msb.framework.web.exception.SentinelBlockExceptionHandler;
import com.msb.framework.web.result.InitializingAdviceDecorator;
import com.msb.framework.web.swagger.SwaggerConfiguration;
import com.msb.framework.web.swagger.SwaggerShortcutController;
import com.msb.framework.web.transform.TransformConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author liao
 */
@Configuration
@ComponentScan(basePackages = "com.msb.framework.web")
@Import({SwaggerConfiguration.class, InitializingAdviceDecorator.class,
        GlobalExceptionHandler.class, SentinelBlockExceptionHandler.class,
        DateTimeConfig.class, SwaggerShortcutController.class, TransformConfig.class, DateTimeConvertPrimaryConfig.class})
public class WebAutoConfig {

}
