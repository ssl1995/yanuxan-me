package com.msb.framework.web.transform.transformer;

import com.msb.framework.web.transform.annotation.Transform;
import org.springframework.lang.NonNull;


/**
 * 建议转换器接口
 *
 * @author R
 */
public interface SimpleTransformer<T> extends Transformer<T, Transform> {
    @Override
    default String transform(@NonNull T originalValue, Transform transform) {
        return transform(originalValue);
    }

    /**
     * 翻译
     *
     * @param originalValue 原始值
     * @param group         分组名
     * @return 翻译后的值
     */
    String transform(@NonNull T originalValue);


}
