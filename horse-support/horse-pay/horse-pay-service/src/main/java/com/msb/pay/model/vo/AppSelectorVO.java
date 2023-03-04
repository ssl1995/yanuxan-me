package com.msb.pay.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("应用选择框VO")
public class AppSelectorVO {

    @ApiModelProperty(value = "应用主键ID")
    private Long appPrimaryId;

    @ApiModelProperty(value = "应用代号")
    private String appCode;

    @ApiModelProperty(value = "应用名称")
    private String appName;

}
