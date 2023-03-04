package com.msb.mall.trade.model.vo.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("APP退款单商品详情VO")
public class RefundProductVO {

    @ApiModelProperty("商品详情ID")
    private Long orderProductId;

    @ApiModelProperty("商品ID")
    private Long productId;

    @ApiModelProperty("商品SKU-ID")
    private Long productSkuId;

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("商品图片")
    private String productImageUrl;

    @ApiModelProperty("SKU规格描述")
    private String skuDescribe;

    @ApiModelProperty("购买数量")
    private Integer quantity;

    @ApiModelProperty("实际价格")
    private BigDecimal realPrice;

    @ApiModelProperty("购买金额")
    private BigDecimal realAmount;

}
