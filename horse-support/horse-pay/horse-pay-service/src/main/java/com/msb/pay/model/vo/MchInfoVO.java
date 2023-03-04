package com.msb.pay.model.vo;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.framework.web.transform.annotation.TransformEnum;
import com.msb.pay.enums.DisabledEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("商户详情信息VO")
public class MchInfoVO extends BaseMchDataVO {

    @ApiModelProperty("商户主键ID")
    private Long mchPrimaryId;

    @ApiModelProperty("商户代号")
    private String mchCode;

    @ApiModelProperty("商户名称")
    private String mchName;

    @ApiModelProperty("商户ID")
    private String mchId;

    @ApiModelPropertyEnum(dictEnum = DisabledEnum.class)
    @ApiModelProperty("商户状态")
    private Boolean isDisabled;

    @TransformEnum(value = DisabledEnum.class, from = "isDisabled")
    @ApiModelProperty("订单状态文本")
    private String isDisabledDesc;

}
