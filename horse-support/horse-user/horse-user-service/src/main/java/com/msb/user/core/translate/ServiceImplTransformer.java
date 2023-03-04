package com.msb.user.core.translate;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.common.utils.SpringContextUtil;
import com.msb.framework.web.transform.TransformUtil;
import com.msb.framework.web.transform.transformer.Transformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Slf4j
@Component
public class ServiceImplTransformer implements Transformer<Long, TransformService> {

    @Override
    public String transform(@Nonnull Long original, @Nonnull TransformService annotation) {
        Class<?> clazz = annotation.service();
        if (ServiceImpl.class.isAssignableFrom(clazz)) {
            IService<?> service = (IService<?>) SpringContextUtil.getBean(clazz);
            Object bean = service.getById(original);
            if (bean == null) {
                log.warn("转换警告：{}类的getById方法，参数：{}，找不到任何数据！", clazz.getSimpleName(), original);
                return null;
            }
            try {
                return (String) TransformUtil.readMethodInvoke(bean, annotation.field());
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException("找不到该属性，请检查注解@TransformService的field字段配置：" + annotation);
            } catch (Exception e) {
                log.error("转换异常：通过ServiceImpl转换失败 " + annotation, e);
                return null;
            }
        }

        return null;
    }
}
