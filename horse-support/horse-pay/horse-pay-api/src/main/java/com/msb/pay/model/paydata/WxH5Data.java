package com.msb.pay.model.paydata;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("微信H5数据")
public class WxH5Data extends PayData implements Serializable {

    @JSONField(name = "h5_url")
    @ApiModelProperty("支付跳转链接")
    private String h5Url;

}
