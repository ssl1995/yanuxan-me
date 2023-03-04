package com.msb.third.config.wx.mp;

import com.msb.framework.web.result.Assert;
import com.msb.third.enums.ThirdExceptionCodeEnum;
import com.msb.third.model.entity.MpApp;
import me.chanjar.weixin.common.redis.RedisTemplateWxRedisOps;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpRedisConfigImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 微信公众号配置初始化
 *
 * @author peng.xy
 * @date 2022/4/28
 */
@Configuration
public class WxMpConfiguration {

    @Resource
    private WxMpConfig wxMpConfig;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Bean
    public WxMpService wxMpService() {
        List<MpApp> configs = wxMpConfig.getConfigs();
        Assert.isTrue(CollectionUtils.isNotEmpty(configs), ThirdExceptionCodeEnum.WX_MP_EXCEPTION);
        Map<String, WxMpConfigStorage> configStorages = configs.stream().map(mpApp -> {
            WxMpRedisConfigImpl wxMpRedisConfig = new WxMpRedisConfigImpl(new RedisTemplateWxRedisOps(stringRedisTemplate), mpApp.getAppId());
            wxMpRedisConfig.setAppId(mpApp.getAppId());
            wxMpRedisConfig.setSecret(mpApp.getSecret());
            wxMpRedisConfig.setToken(mpApp.getToken());
            wxMpRedisConfig.setAesKey(mpApp.getAesKey());
            return wxMpRedisConfig;
        }).collect(Collectors.toMap(WxMpConfigStorage::getAppId, Function.identity()));
        WxMpService service = new WxMpServiceImpl();
        service.setMultiConfigStorages(configStorages);
        return service;
    }

}
