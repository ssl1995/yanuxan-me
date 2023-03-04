package com.msb.pay.model.vo;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.framework.web.transform.annotation.TransformEnum;
import com.msb.pay.enums.DisabledEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("应用详情VO")
public class AppInfoVO extends BaseAppDataVO {

    @ApiModelProperty(value = "应用主键ID")
    private Long appPrimaryId;

    @ApiModelProperty(value = "商户主键ID")
    private Long mchPrimaryId;

    @ApiModelProperty(value = "应用代号")
    private String appCode;

    @ApiModelProperty(value = "支持的支付方式")
    private String payCodes;

    @ApiModelProperty(value = "应用名称")
    private String appName;

    @ApiModelProperty(value = "应用ID")
    private String appId;

    @ApiModelProperty(value = "签名秘钥")
    private String signKey;

    @ApiModelPropertyEnum(dictEnum = DisabledEnum.class)
    @ApiModelProperty(value = "应用状态")
    private Boolean isDisabled;

    @TransformEnum(value = DisabledEnum.class, from = "isDisabled")
    @ApiModelProperty("商户状态文本")
    private String isDisabledDesc;

    @ApiModelProperty(value = "简要商户信息")
    private MchSimpleInfoVO mchInfo;

}
