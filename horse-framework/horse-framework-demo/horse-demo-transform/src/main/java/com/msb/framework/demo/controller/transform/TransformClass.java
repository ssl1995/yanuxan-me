package com.msb.framework.demo.controller.transform;

import com.msb.framework.web.transform.annotation.Transform;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.FIELD})


@Transform(transformer = ClassTransformer.class)
public @interface TransformClass {

    @AliasFor(annotation = Transform.class)
    String from() default "";


}
