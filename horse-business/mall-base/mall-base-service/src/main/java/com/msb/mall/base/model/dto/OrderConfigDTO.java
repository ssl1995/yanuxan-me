package com.msb.mall.base.model.dto;



import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


/**
 * 订单配置表(OrderConfig)表实体类
 *
 * @author shumengjiao
 * @since 2022-06-11 10:54:06
 */
@Data
public class OrderConfigDTO implements Serializable {
    
    @ApiModelProperty("自动收货时长(天)")
    @NotNull
    private Integer automaticReceipt;
    
    @ApiModelProperty("售后过期时长(天)")
    @NotNull
    private Integer afterSalesExpire;
    
    @ApiModelProperty("订单支付失效时长(分钟)")
    @NotNull
    private Integer orderPayExpire;
    
    @ApiModelProperty("退货到期时长(天)")
    @NotNull
    private Integer returnGoodsExpire;
    
    @ApiModelProperty("商家收货到期时长")
    @NotNull
    private Integer merchantReceiptExpire;
    
    @ApiModelProperty("售后申请过期时长(天)")
    @NotNull
    private Integer afterSalesApplicationExpire;

    @ApiModelProperty("自动好评时长(天)")
    @NotNull
    private Integer automaticPraise;
    
}

