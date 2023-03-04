package com.msb.pay.model.vo;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.framework.web.transform.annotation.TransformEnum;
import com.msb.pay.enums.DisabledEnum;
import com.msb.pay.enums.MchCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("商户分页列表VO")
public class MchInfoPageVO {

    @ApiModelProperty("商户主键ID")
    private Long mchPrimaryId;

    @ApiModelProperty("商户代号")
    private String mchCode;

    @TransformEnum(value = MchCodeEnum.class, from = "mchCode")
    @ApiModelProperty("商户代号文本")
    private String mchCodeText;

    @ApiModelProperty("商户名称")
    private String mchName;

    @ApiModelProperty("商户ID")
    private String mchId;

    @ApiModelPropertyEnum(dictEnum = DisabledEnum.class)
    @ApiModelProperty("商户状态")
    private Boolean isDisabled;

    @TransformEnum(value = DisabledEnum.class, from = "isDisabled")
    @ApiModelProperty("商户状态文本")
    private String isDisabledDesc;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

}
