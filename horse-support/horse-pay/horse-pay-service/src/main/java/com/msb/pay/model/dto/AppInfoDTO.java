package com.msb.pay.model.dto;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.pay.enums.DisabledEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("应用信息DTO")
public class AppInfoDTO extends BaseAppDataDTO {

    @NotNull
    @ApiModelProperty(value = "商户主键ID")
    private Long mchPrimaryId;

    @NotBlank
    @Length(max = 64)
    @ApiModelProperty(value = "应用代号")
    private String appCode;

    @NotBlank
    @Length(max = 255)
    @ApiModelProperty(value = "支持的支付方式，逗号分割")
    private String payCodes;

    @NotBlank
    @Length(max = 64)
    @ApiModelProperty(value = "签名秘钥")
    private String signKey;

    @NotBlank
    @Length(max = 64)
    @ApiModelProperty(value = "应用名称")
    private String appName;

    @NotBlank
    @Length(max = 64)
    @ApiModelProperty(value = "应用ID")
    private String appId;

    @NotNull
    @ApiModelPropertyEnum(dictEnum = DisabledEnum.class)
    @ApiModelProperty(value = "应用状态")
    private Boolean isDisabled;

}
