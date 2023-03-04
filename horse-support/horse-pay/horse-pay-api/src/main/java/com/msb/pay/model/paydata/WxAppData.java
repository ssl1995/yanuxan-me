package com.msb.pay.model.paydata;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("微信APP数据")
public class WxAppData extends PayData implements Serializable {

    @JSONField(name = "appid")
    @ApiModelProperty("应用ID")
    private String appId;

    @JSONField(name = "partnerid")
    @ApiModelProperty("商户ID")
    private String partnerId;

    @JSONField(name = "prepayid")
    @ApiModelProperty("预支付ID")
    private String prepayId;

    @JSONField(name = "package")
    @ApiModelProperty("订单详情扩展字符串")
    private String packageValue;

    @JSONField(name = "timestamp")
    @ApiModelProperty("时间戳")
    private String timeStamp;

    @JSONField(name = "noncestr")
    @ApiModelProperty("随机数")
    private String nonceStr;

    @ApiModelProperty("支付签名")
    private String sign;

}
