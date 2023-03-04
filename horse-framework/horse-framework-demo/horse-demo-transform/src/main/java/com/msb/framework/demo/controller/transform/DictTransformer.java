package com.msb.framework.demo.controller.transform;

import com.msb.framework.demo.service.DictionaryService;
import com.msb.framework.web.transform.transformer.Transformer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import java.util.Optional;

/**
 * 数据字典翻译
 *
 * @author luozhan
 */
@Component
@ConditionalOnBean(DictionaryService.class)
public class DictTransformer implements Transformer<Integer, TransformDict> {

    /**
     * 字典服务
     */
    @Resource
    private DictionaryService dictionaryService;

    @Override
    public String transform(@Nonnull Integer code, TransformDict transformDict) {
        String text = dictionaryService.getText(transformDict.group(), code);
        return Optional.ofNullable(text).orElse("");
    }
}
