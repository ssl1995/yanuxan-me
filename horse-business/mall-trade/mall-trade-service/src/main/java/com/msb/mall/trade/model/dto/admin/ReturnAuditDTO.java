package com.msb.mall.trade.model.dto.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@ApiModel("后管退货审核DTO")
public class ReturnAuditDTO {

    @NotNull
    @ApiModelProperty(value = "退款单ID", required = true)
    private Long refundId;

    @NotNull
    @DecimalMin("0.01")
    @ApiModelProperty(value = "实际退款金额", required = true)
    private BigDecimal refundAmount;

    @NotBlank
    @Length(max = 64)
    @ApiModelProperty(value = "收件人姓名", required = true)
    private String recipientName;

    @NotBlank
    @Length(max = 64)
    @ApiModelProperty(value = "收件人号码", required = true)
    private String recipientPhone;

    @NotBlank
    @Length(max = 32)
    @ApiModelProperty(value = "省区域代码", required = true)
    private String provinceCode;

    @NotBlank
    @Length(max = 32)
    @ApiModelProperty(value = "省", required = true)
    private String province;

    @NotBlank
    @Length(max = 32)
    @ApiModelProperty(value = "市区域代码", required = true)
    private String cityCode;

    @NotBlank
    @Length(max = 32)
    @ApiModelProperty(value = "市", required = true)
    private String city;

    @NotBlank
    @Length(max = 32)
    @ApiModelProperty(value = "区/县代码", required = true)
    private String areaCode;

    @NotBlank
    @Length(max = 32)
    @ApiModelProperty(value = "区/县", required = true)
    private String area;

    @NotBlank
    @Length(max = 128)
    @ApiModelProperty(value = "详细地址", required = true)
    private String detailAddress;

    @Length(max = 64)
    @ApiModelProperty(value = "备注", required = false)
    private String remark;

}
