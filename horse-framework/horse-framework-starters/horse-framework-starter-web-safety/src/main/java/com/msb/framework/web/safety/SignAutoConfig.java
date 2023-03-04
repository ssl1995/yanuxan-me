package com.msb.framework.web.safety;

import com.msb.framework.web.safety.sign.SignAspect;
import com.msb.framework.web.safety.sign.SignExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author liao
 */
@Configuration
@Import({SignAspect.class, SignExceptionHandler.class})
public class SignAutoConfig {

}
