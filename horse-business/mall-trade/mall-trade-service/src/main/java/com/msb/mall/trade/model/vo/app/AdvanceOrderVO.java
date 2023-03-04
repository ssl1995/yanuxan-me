package com.msb.mall.trade.model.vo.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel("预订单VO")
public class AdvanceOrderVO {

    @ApiModelProperty("服务器时间")
    private LocalDateTime serverTime;

    @ApiModelProperty("支付失效时间")
    private LocalDateTime expireTime;

    @ApiModelProperty("总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty("实际商品金额")
    private BigDecimal productAmount;

    @ApiModelProperty("运费")
    private BigDecimal shippingAmount;

    @ApiModelProperty("优惠金额")
    private BigDecimal discountAmount;

    @ApiModelProperty("实付金额")
    private BigDecimal payAmount;

    @ApiModelProperty("商品详情信息")
    private List<AdvanceOrderProductVO> products;

}
