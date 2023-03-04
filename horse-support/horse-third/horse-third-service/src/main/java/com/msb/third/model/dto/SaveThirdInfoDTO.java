package com.msb.third.model.dto;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.third.enums.PlatformEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("保存用户第三方授权信息DTO")
public class SaveThirdInfoDTO {

    /**
     * 平台类型（1：微信）
     */
    @Min(1)
    @Max(1)
    @NotNull
    @ApiModelPropertyEnum(dictEnum = PlatformEnum.class)
    @ApiModelProperty(value = "平台类型", required = true)
    private Integer platformType;

    /**
     * 平台应用ID
     */
    @NotBlank
    @ApiModelProperty(value = "平台应用ID", required = true)
    private String appId;

    /**
     * 平台用户ID
     */
    @NotBlank
    @ApiModelProperty(value = "平台用户ID", required = true)
    private String appUserId;

}
