package com.msb.pay.model.dto;

import com.msb.framework.common.model.PageDTO;
import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.pay.enums.NotifyStatusEnum;
import com.msb.pay.enums.RefundStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("退款订单分页列表DTO")
public class RefundOrderPageDTO extends PageDTO {

    @ApiModelProperty("商户主键ID")
    private Long mchPrimaryId;

    @ApiModelProperty("应用主键ID")
    private Long appPrimaryId;

    @ApiModelProperty("支付订单号")
    private String payOrderNo;

    @ApiModelProperty("退款订单号")
    private String refundOrderNo;

    @ApiModelPropertyEnum(dictEnum = RefundStatusEnum.class)
    @ApiModelProperty("退款状态")
    private Integer refundStatus;

    @ApiModelPropertyEnum(dictEnum = NotifyStatusEnum.class)
    @ApiModelProperty("通知状态")
    private Integer notifyStatus;

    @ApiModelProperty(value = "申请开始时间（yyyy-MM-dd HH:mm:ss）")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "申请结束时间（yyyy-MM-dd HH:mm:ss）")
    private LocalDateTime endTime;

}
