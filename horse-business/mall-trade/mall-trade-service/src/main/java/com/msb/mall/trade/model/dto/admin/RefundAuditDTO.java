package com.msb.mall.trade.model.dto.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("后管退款审核DTO")
public class RefundAuditDTO {

    @NotNull
    @ApiModelProperty(value = "退款单ID", required = true)
    private Long refundId;

    @NotNull
    @DecimalMin("0.01")
    @ApiModelProperty(value = "实际退款金额", required = true)
    private BigDecimal refundAmount;

    @Length(max = 64)
    @ApiModelProperty(value = "备注", required = false)
    private String remark;

}
