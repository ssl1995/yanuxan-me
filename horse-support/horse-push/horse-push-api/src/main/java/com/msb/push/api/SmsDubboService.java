package com.msb.push.api;

import com.msb.push.enums.SmsTemplateEnum;

/**
 * 短信服务Dubbo接口
 *
 * @author peng.xy
 * @date 2022/5/16
 */
public interface SmsDubboService {

    /**
     * 发送短信
     *
     * @param phone：手机号
     * @param templateEnum：短信模板
     * @param params：短信内容变量值
     * @return java.lang.Boolean
     * @author peng.xy
     * @date 2022/5/16
     */
    Boolean sendSms(String phone, SmsTemplateEnum templateEnum, String... params);

}
