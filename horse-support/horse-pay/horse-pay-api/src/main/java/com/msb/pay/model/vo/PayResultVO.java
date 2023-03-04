package com.msb.pay.model.vo;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.framework.web.transform.annotation.TransformEnum;
import com.msb.pay.enums.PayStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("支付结果VO")
public class PayResultVO {

    @ApiModelProperty("支付订单ID")
    private Long payOrderId;

    @ApiModelProperty("支付订单号")
    private String payOrderNo;

    @ApiModelProperty("支付方式")
    private String payCode;

    @ApiModelProperty("回调页面")
    private String returnUrl;

    @ApiModelPropertyEnum(dictEnum = PayStatusEnum.class)
    @ApiModelProperty("支付状态")
    private Integer payStatus;

    @TransformEnum(value = PayStatusEnum.class, from = "payStatus")
    @ApiModelProperty("支付方式文本")
    private String payStatusText;

}
