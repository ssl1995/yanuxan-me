package com.msb.sensitive.dubbo;

import com.msb.sensitive.api.dubbo.SensitiveWordsDubboService;
import com.msb.sensitive.api.model.SensitiveWordsDO;
import com.msb.sensitive.service.SensitiveWordsService;
import com.msb.sensitive.service.SensitiveWordsUtilService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author 86151
 */
@DubboService
public class SensitiveWordsDubboServiceImpl implements SensitiveWordsDubboService {

    @Resource
    private SensitiveWordsService sensitiveWordsService;

    @Resource
    private SensitiveWordsUtilService sensitiveWordsUtilService;

    @Override
    public SensitiveWordsDO checkSensitiveWordsAndReplace(String content) {
        return sensitiveWordsService.checkIfContainSensitive(content);
    }

    @Override
    public Boolean checkIfContainSensitiveWords(String content) {
        return sensitiveWordsUtilService.contains(content);
    }
}
