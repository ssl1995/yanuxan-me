package com.msb.user.auth;

import java.lang.annotation.*;

/**
 * @author liao
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface NoAuth {

}
