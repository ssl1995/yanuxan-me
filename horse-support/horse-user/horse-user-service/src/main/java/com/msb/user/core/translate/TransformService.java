package com.msb.user.core.translate;

import com.baomidou.mybatisplus.extension.service.IService;
import com.msb.framework.web.transform.annotation.Transform;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.FIELD})

@Transform(transformer = ServiceImplTransformer.class)
public @interface TransformService {

    Class<? extends IService<?>> service();

    String field();

    @AliasFor(annotation = Transform.class)
    String from() default "";

}