package com.msb.pay.model.dto;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.pay.enums.PayCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
@ApiModel("收银台支付DTO")
public class CashierPayDTO {

    @NotBlank
    @ApiModelProperty(value = "支付订单号", required = true)
    private String payOrderNo;

    @NotBlank
    @Length(max = 64)
    @ApiModelProperty(value = "应用代号", required = true)
    private String appCode;

    @NotBlank
    @Length(max = 64)
    @ApiModelPropertyEnum(dictEnum = PayCodeEnum.class)
    @ApiModelProperty(value = "支付方式", required = true)
    private String payCode;

    @ApiModelProperty(value = "渠道用户ID（微信openId）")
    private String channelUser;

}
