package com.msb.mall.trade.model.dto.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@ApiModel("APP填写退货信息DTO")
public class RefundCompleteDTO {

    @NotNull
    @ApiModelProperty(value = "退款单ID", required = true)
    private Long refundId;

    @NotBlank
    @Length(max = 32)
    @ApiModelProperty(value = "物流公司编号", required = true)
    private String companyCode;

    @NotBlank
    @Length(max = 32)
    @ApiModelProperty(value = "物流公司名称", required = true)
    private String companyName;

    @NotBlank
    @Length(max = 32)
    @ApiModelProperty(value = "物流单号", required = true)
    private String trackingNo;

    @Length(max = 64)
    @ApiModelProperty(value = "备注", required = false)
    private String remark;

    @Size(max = 9)
    @ApiModelProperty(value = "物流凭证图片地址数组", required = false)
    private String[] logisticsImages;

}
