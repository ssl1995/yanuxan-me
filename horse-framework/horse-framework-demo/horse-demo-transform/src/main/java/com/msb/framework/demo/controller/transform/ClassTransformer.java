package com.msb.framework.demo.controller.transform;

import com.msb.framework.demo.service.ClassService;
import com.msb.framework.web.transform.transformer.SimpleTransformer;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import java.util.Optional;

/**
 * 班级名称翻译
 */
@Component
public class ClassTransformer implements SimpleTransformer<Long> {

    @Resource
    private ClassService classService;

    @Override
    public String transform(@Nonnull Long classId) {
        String className = classService.getName(classId);
        return Optional.ofNullable(className).orElse(String.valueOf(classId));
    }
}
