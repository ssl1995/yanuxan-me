package com.msb.framework.web.safety.repeat;

import java.lang.annotation.*;

/**
 * 防止重复提交
 *
 * @author liao
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.METHOD)
public @interface UnRepeatable {

}
