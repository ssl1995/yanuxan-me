package com.msb.pay.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("商户简要信息VO")
public class MchSimpleInfoVO {

    @ApiModelProperty("商户主键ID")
    private Long mchPrimaryId;

    @ApiModelProperty("商户代号")
    private String mchCode;

    @ApiModelProperty("商户名称")
    private String mchName;

}
