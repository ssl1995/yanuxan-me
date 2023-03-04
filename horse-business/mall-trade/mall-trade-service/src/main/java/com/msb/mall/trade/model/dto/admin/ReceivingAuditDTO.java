package com.msb.mall.trade.model.dto.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("后管收货审核DTO")
public class ReceivingAuditDTO {

    @NotNull
    @ApiModelProperty(value = "退款单ID", required = true)
    private Long refundId;

    @Length(max = 64)
    @ApiModelProperty(value = "备注", required = false)
    private String remark;

}
