package com.msb.mall.trade.model.vo.admin;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.framework.web.tool.excel.LocalDateStringConverter;
import com.msb.framework.web.transform.annotation.TransformEnum;
import com.msb.mall.trade.enums.OrderPayTypeEnum;
import com.msb.mall.trade.enums.OrderSourceEnum;
import com.msb.mall.trade.enums.OrderStatusEnum;
import com.msb.mall.trade.enums.OrderTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ExcelIgnoreUnannotated
@ApiModel("后管订单列表VO")
public class OrderListAdminVO {

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ExcelProperty("订单编号")
    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ExcelProperty("用户号码")
    @ApiModelProperty("用户号码")
    private String userPhone;

    @ExcelProperty("用户留言")
    @ApiModelProperty("用户留言")
    private String userMessage;

    @ExcelProperty("实付金额")
    @ApiModelProperty("实付金额")
    private BigDecimal payAmount;

    @ApiModelPropertyEnum(dictEnum = OrderStatusEnum.class)
    @ApiModelProperty("订单状态")
    private Integer orderStatus;

    @ExcelProperty("订单状态")
    @TransformEnum(value = OrderStatusEnum.class, from = "orderStatus")
    @ApiModelProperty("订单状态文本")
    private String orderStatusDesc;

    @ApiModelPropertyEnum(dictEnum = OrderPayTypeEnum.class)
    @ApiModelProperty("支付方式")
    private Integer payType;

    @ExcelProperty("支付方式")
    @TransformEnum(value = OrderPayTypeEnum.class, from = "payType")
    @ApiModelProperty("支付方式文本")
    private String payTypeDesc;

    @ApiModelPropertyEnum(dictEnum = OrderSourceEnum.class)
    @ApiModelProperty("订单来源")
    private Integer orderSource;

    @ExcelProperty("订单来源")
    @TransformEnum(value = OrderSourceEnum.class, from = "orderSource")
    @ApiModelProperty("订单来源文本")
    private String orderSourceDesc;

    @ApiModelPropertyEnum(dictEnum = OrderTypeEnum.class)
    @ApiModelProperty("订单类型")
    private Integer orderType;

    @TransformEnum(value = OrderTypeEnum.class, from = "orderType")
    @ApiModelProperty("订单类型文本")
    private String orderTypeDesc;

    @ExcelProperty(value = "下单时间", converter = LocalDateStringConverter.class)
    @ApiModelProperty(value = "下单时间")
    private LocalDateTime submitTime;

}
