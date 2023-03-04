package com.msb.pay.model.vo;

import com.msb.pay.model.BaseSign;
import com.msb.pay.model.paydata.PayData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("统一下单VO")
public class UnifiedOrderVO<T extends PayData> extends BaseSign implements Serializable {

    @ApiModelProperty("支付代号")
    private String mchCode;

    @ApiModelProperty("应用代号")
    private String appCode;

    @ApiModelProperty("应用ID")
    private String appId;

    @ApiModelProperty("支付方式")
    private String payCode;

    @ApiModelProperty("支付订单ID")
    private String payOrderId;

    @ApiModelProperty("支付订单号")
    private String payOrderNo;

    @ApiModelProperty("支付数据json")
    private String payData;

    @ApiModelProperty("支付数据")
    private T payDataInfo;

}
