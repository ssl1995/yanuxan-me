package com.msb.pay.model.bo;

import com.msb.pay.model.paydata.PayData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel("统一下单响应")
public class UnifiedOrderRES<T extends PayData> implements Serializable {

    @ApiModelProperty("支付代号")
    private String mchCode;

    @ApiModelProperty("应用代号")
    private String appCode;

    @ApiModelProperty("应用ID")
    private String appId;

    @ApiModelProperty("支付方式")
    private String payCode;

    @ApiModelProperty("支付订单号")
    private String payOrderNo;

    @ApiModelProperty("支付数据json")
    private String payData;

    @ApiModelProperty("支付数据")
    private T payDataInfo;

    @ApiModelProperty("支付渠道发起参数")
    private String channelRequest;

    @ApiModelProperty("支付渠道响应参数")
    private String channelResponse;

}
