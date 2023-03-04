package com.msb.pay.model.dto;

import com.msb.framework.common.model.PageDTO;
import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.pay.enums.NotifyStatusEnum;
import com.msb.pay.enums.PayStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("支付订单分页列表DTO")
public class PayOrderPageDTO extends PageDTO {

    @ApiModelProperty("商户主键ID")
    private Long mchPrimaryId;

    @ApiModelProperty("应用主键ID")
    private Long appPrimaryId;

    @ApiModelProperty("支付订单号")
    private String payOrderNo;

    @ApiModelProperty("支付方式")
    private String payCode;

    @ApiModelPropertyEnum(dictEnum = PayStatusEnum.class)
    @ApiModelProperty("支付状态")
    private Integer payStatus;

    @ApiModelPropertyEnum(dictEnum = NotifyStatusEnum.class)
    @ApiModelProperty("通知状态")
    private Integer notifyStatus;

    @ApiModelProperty(value = "下单开始时间（yyyy-MM-dd HH:mm:ss）")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "下单结束时间（yyyy-MM-dd HH:mm:ss）")
    private LocalDateTime endTime;

}
