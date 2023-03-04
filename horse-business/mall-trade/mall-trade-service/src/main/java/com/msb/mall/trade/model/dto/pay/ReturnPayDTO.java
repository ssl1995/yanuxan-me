package com.msb.mall.trade.model.dto.pay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("页面回调支付DTO")
public class ReturnPayDTO extends BasePayDTO {

    //    @NotBlank
    @ApiModelProperty(value = "回调页面地址", required = true)
    private String returnUrl;

}
