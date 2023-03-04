package com.msb.third.model.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Map;

@Data
@ApiModel("公众号应用")
public class MpApp {

    /**
     * appCode
     */
    private String appCode;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 开发者密码
     */
    private String secret;

    /**
     * 令牌token
     */
    private String token;

    /**
     * 解密密钥
     */
    private String aesKey;

    /**
     * 消息模板Map
     */
    private Map<String, MpMessageTemplate> templateMap;

}
