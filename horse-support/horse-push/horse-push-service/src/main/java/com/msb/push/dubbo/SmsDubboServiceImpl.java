package com.msb.push.dubbo;

import com.msb.push.api.SmsDubboService;
import com.msb.push.enums.SmsTemplateEnum;
import com.msb.push.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@Slf4j
@DubboService
public class SmsDubboServiceImpl implements SmsDubboService {

    @Resource
    private SmsService smsService;

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
    @Override
    public Boolean sendSms(String phone, SmsTemplateEnum templateEnum, String... params) {
        return smsService.sendSms(phone, templateEnum, params);
    }

}
