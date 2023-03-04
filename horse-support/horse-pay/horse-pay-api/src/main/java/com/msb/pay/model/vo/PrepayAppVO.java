package com.msb.pay.model.vo;

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
@ApiModel("预支付应用VO")
public class PrepayAppVO {

    @ApiModelProperty("预支付应用ID")
    private String prepayAppId;

    @ApiModelProperty("预支付应用代号")
    private String prepayAppCode;

}
