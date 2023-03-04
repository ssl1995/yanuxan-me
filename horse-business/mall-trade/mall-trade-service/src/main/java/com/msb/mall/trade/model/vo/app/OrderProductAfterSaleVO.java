package com.msb.mall.trade.model.vo.app;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.framework.web.transform.annotation.TransformEnum;
import com.msb.mall.trade.enums.OrderProductDetailEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("APP订单商品售后VO")
public class OrderProductAfterSaleVO {

    @ApiModelProperty("商品详情ID")
    private Long orderProductId;

    @ApiModelProperty("订单ID")
    private Long orderId;

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

    @ApiModelProperty("实际金额")
    private BigDecimal realAmount;

    @ApiModelPropertyEnum(dictEnum = OrderProductDetailEnum.class)
    @ApiModelProperty("商品明细状态")
    private Integer detailStatus;

    @TransformEnum(value = OrderProductDetailEnum.class, from = "detailStatus")
    @ApiModelProperty("商品明细状态文本")
    private String detailStatusDesc;

    @ApiModelProperty("订单商品详情是否可申请售后")
    private Boolean afterSaleApplyFlag;

    public OrderProductAfterSaleVO() {
        afterSaleApplyFlag = false;
    }
}
