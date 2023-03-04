package com.msb.pay.model.dto;

import com.msb.pay.model.BaseSign;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@ApiModel("退款申请DTO")
public class ApplyRefundDTO extends BaseSign implements Serializable {

    @NotBlank
    @Length(max = 64)
    @ApiModelProperty(value = "应用代号", required = true)
    private String appCode;

    @NotBlank
    @ApiModelProperty(value = "支付订单号", required = true)
    private String payOrderNo;

    @NotBlank
    @ApiModelProperty(value = "退款订单号", required = true)
    private String refundOrderNo;

    @NotNull
    @DecimalMin("0.01")
    @ApiModelProperty(value = "退款金额", required = true)
    private BigDecimal refundAmount;

    @NotBlank
    @ApiModelProperty(value = "退款原因", required = true)
    private String refundReason;

    @ApiModelProperty(value = "回调通知地址（为空则采用MQ进行通知）")
    private String notifyUrl;

}
