package com.msb.pay.model.dto;

import com.msb.framework.common.model.PageDTO;
import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.pay.enums.DisabledEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("应用分页列表DTO")
public class AppInfoPageDTO extends PageDTO {

    @ApiModelProperty("商户主键ID")
    private Long mchPrimaryId;

    @ApiModelProperty("应用名称")
    private String appName;

    @ApiModelProperty("应用ID")
    private String appId;

    @ApiModelProperty("支持的支付方式")
    private String payCode;

    @ApiModelPropertyEnum(dictEnum = DisabledEnum.class)
    @ApiModelProperty("应用状态")
    private Boolean isDisabled;

}
