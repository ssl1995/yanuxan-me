package com.msb.user.auth;

import io.jsonwebtoken.impl.DefaultClaims;

import java.lang.annotation.*;

/**
 * @author liao
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface AuthAdmin {
    boolean flag() default true;
}
