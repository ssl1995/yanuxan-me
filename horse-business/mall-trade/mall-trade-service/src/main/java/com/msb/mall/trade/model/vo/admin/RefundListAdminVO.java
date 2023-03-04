package com.msb.mall.trade.model.vo.admin;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.framework.web.tool.excel.LocalDateStringConverter;
import com.msb.framework.web.transform.annotation.TransformEnum;
import com.msb.mall.trade.enums.RefundHandleEnum;
import com.msb.mall.trade.enums.RefundStatusEnum;
import com.msb.mall.trade.enums.RefundTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ExcelIgnoreUnannotated
@ApiModel("后管退款单列表VO")
public class RefundListAdminVO {

    @ApiModelProperty("退款单ID")
    private Long refundId;

    @ExcelProperty("退款单号")
    @ApiModelProperty("退款单号")
    private String refundNo;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ExcelProperty("用户号码")
    @ApiModelProperty("用户号码")
    private String userPhone;

    @ExcelProperty("申请退款金额")
    @ApiModelProperty("申请退款金额")
    private BigDecimal applyAmount;

    @ExcelProperty("实际退款金额")
    @ApiModelProperty("实际退款金额")
    private BigDecimal refundAmount;

    @ApiModelPropertyEnum(dictEnum = RefundStatusEnum.class)
    @ApiModelProperty("退款单状态")
    private Integer refundStatus;

    @ExcelProperty("退款单状态")
    @ApiModelProperty("退款单状态文本")
    @TransformEnum(value = RefundStatusEnum.class, from = "refundStatus")
    private String refundStatusDesc;

    @ApiModelPropertyEnum(dictEnum = RefundTypeEnum.class)
    @ApiModelProperty("退款单类型")
    private Integer refundType;

    @ExcelProperty("退款单类型")
    @TransformEnum(value = RefundTypeEnum.class, from = "refundType")
    @ApiModelProperty("退款单类型文本")
    private String refundTypeDesc;

    @ApiModelPropertyEnum(dictEnum = RefundHandleEnum.class)
    @ApiModelProperty("商家处理状态")
    private Integer handleStatus;

    @ExcelProperty("商家处理状态")
    @ApiModelProperty("商家处理状态文本")
    @TransformEnum(value = RefundHandleEnum.class, from = "handleStatus")
    private String handleStatusDesc;

    @ExcelProperty(value = "退款申请时间", converter = LocalDateStringConverter.class)
    @ApiModelProperty("退款申请时间")
    private LocalDateTime applyTime;

    @ExcelProperty(value = "退款处理时间", converter = LocalDateStringConverter.class)
    @ApiModelProperty("退款处理时间")
    private LocalDateTime handleTime;

}
