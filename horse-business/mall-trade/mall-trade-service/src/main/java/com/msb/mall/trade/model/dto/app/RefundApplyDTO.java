package com.msb.mall.trade.model.dto.app;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.mall.trade.enums.RefundReasonEnum;
import com.msb.mall.trade.enums.RefundReceiveStatusDesc;
import com.msb.mall.trade.enums.RefundTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@ApiModel("APP申请退款DTO")
public class RefundApplyDTO {

    @NotNull
    @ApiModelProperty(value = "订单商品详情ID", required = true)
    private Long orderProductId;

    @NotNull
    @Min(1)
    @Max(2)
    @ApiModelPropertyEnum(dictEnum = RefundTypeEnum.class)
    @ApiModelProperty(value = "退款单类型", required = true)
    private Integer refundType;

    @NotNull
    @Min(1)
    @Max(2)
    @ApiModelPropertyEnum(dictEnum = RefundReceiveStatusDesc.class)
    @ApiModelProperty(value = "收货状态，如退款单类型refundType为【退货退款】，则收货状态应固定为【已收到货】", required = true)
    private Integer receiveStatus;

    @NotNull
    @Min(1)
    @Max(4)
    @ApiModelPropertyEnum(dictEnum = RefundReasonEnum.class)
    @ApiModelProperty(value = "退款原因", required = true)
    private Integer refundReason;

    @Length(max = 64)
    @ApiModelProperty(value = "问题描述", required = false)
    private String problemDescribe;

    @Size(max = 9)
    @ApiModelProperty(value = "凭证图片地址", required = false)
    private String[] evidenceImages;

}
