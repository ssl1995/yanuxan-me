package com.msb.pay.model.paydata;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("微信NATIVE数据")
public class WxNativeData extends PayData implements Serializable {

    @JSONField(name = "code_url")
    @ApiModelProperty("付款二维码数据")
    private String codeUrlData;

}
