package com.msb.mall.trade.model.dto.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Objects;

@Data
@Accessors(chain = true)
@ApiModel("APP退款单详情查询VO")
public class RefundInfoDTO {

    @ApiModelProperty(value = "退款单ID（二选一必传）")
    private Long refundId;

    @ApiModelProperty(value = "订单商品详情ID（二选一必传）")
    private Long orderProductId;

    public boolean validate() {
        return Objects.nonNull(refundId) || Objects.nonNull(orderProductId);
    }

}
