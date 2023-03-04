package com.msb.framework.web.transform.unwrapper;

import org.springframework.core.convert.converter.Converter;

import javax.annotation.Nonnull;

/**
 * 解包器
 * <p>
 * 当方法返回值是包装类（如Page、ResultWrapper等）时，指定解包的逻辑
 * 注意解包之后的返回参数必须是某个bean或者集合类型
 *
 * @author R
 * @date 2023-3-1
 */
public interface UnWrapper<T> extends Converter<T, Object> {

    /**
     * 解包
     *
     * @param source 源
     * @return 包装类内的实际对象
     */
    Object unWrap(T source);

    /**
     * 将convert更名为unWrap，语义化更好
     *
     * @param source 源
     * @return 目标对象
     */
    @Override
    default Object convert(@Nonnull T source) {
        return unWrap(source);
    }
}
