package com.msb.framework.web.safety.sign;

import java.lang.annotation.*;

/**
 * @author R
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.METHOD)
public @interface Sign {
    String signKey() default "s";

    String timeStampKey() default "t";

    int timeout() default 5;

    boolean hideError() default false;
}
