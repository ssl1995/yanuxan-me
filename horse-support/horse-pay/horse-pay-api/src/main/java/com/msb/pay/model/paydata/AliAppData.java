package com.msb.pay.model.paydata;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("支付宝APP数据")
public class AliAppData extends PayData implements Serializable {

    @ApiModelProperty("支付数据")
    private String payData;

}
