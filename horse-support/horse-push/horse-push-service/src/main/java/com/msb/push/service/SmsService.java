package com.msb.push.service;

import com.alibaba.fastjson.JSON;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.msb.framework.common.exception.BizException;
import com.msb.push.config.sms.SmsConfiguration;
import com.msb.push.enums.SmsTemplateEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author liao
 */
@Slf4j
@Service("smsService")
public class SmsService {

    @Resource
    private SmsConfiguration smsConfiguration;

    @Resource
    private Client client;

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
    public Boolean sendSms(String phone, SmsTemplateEnum templateEnum, String... params) {
        SmsConfiguration.Template templateParam = smsConfiguration.getTemplateMap().get(templateEnum.getMessageCode());
        Map<String, String> templateParamValueMap = new HashMap<>(params.length);
        LinkedList<String> paramValuesList = new LinkedList<>(Arrays.asList(params));
        templateParam.getParamNameList().forEach(s -> templateParamValueMap.put(s, paramValuesList.poll()));
        SendSmsRequest request = new SendSmsRequest();
        try {
            request.setPhoneNumbers(phone);
            request.setSignName(smsConfiguration.getSignName());
            request.setTemplateCode(templateParam.getTemplateCode());
            request.setTemplateParam(JSON.toJSONString(templateParamValueMap));
            SendSmsResponse acsResponse = client.sendSms(request);
            log.info("发送短信结果：{}", JSON.toJSONString(acsResponse));
        } catch (Exception e) {
            log.error("发送短信失败：{}", JSON.toJSONString(request), e);
            throw new BizException("发送短信失败，请稍后重试");
        }
        return Boolean.TRUE;
    }

}
