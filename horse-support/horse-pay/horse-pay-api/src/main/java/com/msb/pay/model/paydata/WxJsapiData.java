package com.msb.pay.model.paydata;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("微信公众号数据")
public class WxJsapiData extends PayData implements Serializable {

    @ApiModelProperty("应用ID")
    private String appId;

    @ApiModelProperty("时间戳")
    private String timeStamp;

    @ApiModelProperty("随机字符串")
    private String nonceStr;

    @JSONField(name = "package")
    @ApiModelProperty("订单详情扩展字符串")
    private String packageValue;

    @ApiModelProperty("签名方式")
    private String signType;

    @ApiModelProperty("签名")
    private String paySign;

}
