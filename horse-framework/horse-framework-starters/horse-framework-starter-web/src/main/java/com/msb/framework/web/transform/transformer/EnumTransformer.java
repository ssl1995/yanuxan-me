package com.msb.framework.web.transform.transformer;

import com.msb.framework.common.model.IDict;
import com.msb.framework.web.transform.annotation.TransformEnum;

import javax.annotation.Nonnull;


/**
 * 枚举转换器
 *
 * @author R
 */
//@Component
public class EnumTransformer<T> implements Transformer<T, TransformEnum> {

    @Override
    @SuppressWarnings("unchecked")
    public String transform(@Nonnull T enumCode, @Nonnull TransformEnum annotation) {
        return IDict.getTextByCode((Class<? extends IDict<T>>) annotation.value(), enumCode);
    }


}
