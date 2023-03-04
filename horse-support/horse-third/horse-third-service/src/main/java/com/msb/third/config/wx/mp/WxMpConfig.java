package com.msb.third.config.wx.mp;

import com.msb.framework.common.exception.BizException;
import com.msb.framework.web.result.Assert;
import com.msb.third.enums.ThirdExceptionCodeEnum;
import com.msb.third.enums.WxMpAppEnum;
import com.msb.third.enums.WxMpAppMessageTemplateEnum;
import com.msb.third.model.entity.MpApp;
import com.msb.third.model.entity.MpMessageTemplate;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.*;

/**
 * 微信公众号配置信息
 *
 * @author peng.xy
 * @date 2022/4/28
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "wx.mp")
public class WxMpConfig {

    /**
     * 公众号配置列表
     */
    private List<MpApp> configs;

    /**
     * 根据枚举类获取微信配置应用
     *
     * @param wxMpAppEnum：微信公众号枚举
     * @return com.msb.mall.third.model.entity.MpApp
     * @author peng.xy
     * @date 2022/5/12
     */
    public MpApp getMpAppByMpEnum(WxMpAppEnum wxMpAppEnum) {
        Assert.isTrue(CollectionUtils.isNotEmpty(configs), ThirdExceptionCodeEnum.WX_MP_EXCEPTION);
        Optional<MpApp> first = configs.stream().filter(mpApp -> Objects.equals(mpApp.getAppCode(), wxMpAppEnum.getAppCode())).findFirst();
        return first.orElseThrow(() -> new BizException(ThirdExceptionCodeEnum.WX_APPID_EXCEPTION));
    }

    /**
     * 根据appId获取微信配置应用
     *
     * @param appId：公众号应用ID
     * @return com.msb.mall.third.model.entity.MpApp
     * @author peng.xy
     * @date 2022/5/12
     */
    public MpApp getMpAppByAppId(String appId) {
        Assert.isTrue(CollectionUtils.isNotEmpty(configs), ThirdExceptionCodeEnum.WX_MP_EXCEPTION);
        Optional<MpApp> first = configs.stream().filter(mpApp -> Objects.equals(mpApp.getAppId(), appId)).findFirst();
        return first.orElseThrow(() -> new BizException(ThirdExceptionCodeEnum.WX_APPID_EXCEPTION));
    }

    /**
     * 根据appId获取枚举类
     *
     * @param appId：公众号应用ID
     * @return com.msb.mall.third.enums.WxMpEnum
     * @author peng.xy
     * @date 2022/5/12
     */
    public WxMpAppEnum getMpEnumByAppId(String appId) {
        MpApp mpApp = this.getMpAppByAppId(appId);
        Optional<WxMpAppEnum> first = Arrays.stream(WxMpAppEnum.values()).filter(wxMpAppEnum -> Objects.equals(wxMpAppEnum.getAppCode(), mpApp.getAppCode())).findFirst();
        return first.orElseThrow(() -> new BizException(ThirdExceptionCodeEnum.WX_APPID_EXCEPTION));
    }

    /**
     * 根据appId和模板枚举获取消息模板
     *
     * @param appId：公众号应用ID
     * @param wxMpAppMessageTemplateEnum：公众号消息模板枚举
     * @return com.msb.third.model.entity.MpMessageTemplate
     * @author peng.xy
     * @date 2022/5/17
     */
    public MpMessageTemplate getMpMessageTemplate(String appId, WxMpAppMessageTemplateEnum wxMpAppMessageTemplateEnum) {
        MpApp mpApp = this.getMpAppByAppId(appId);
        Map<String, MpMessageTemplate> templateMap = mpApp.getTemplateMap();
        MpMessageTemplate messageTemplate = templateMap.get(wxMpAppMessageTemplateEnum.getCode());
        if (Objects.isNull(messageTemplate)) {
            throw new BizException(ThirdExceptionCodeEnum.WX_MP_MESSAGE_TEMPLATE_EXCEPTION);
        }
        return messageTemplate;
    }

}
