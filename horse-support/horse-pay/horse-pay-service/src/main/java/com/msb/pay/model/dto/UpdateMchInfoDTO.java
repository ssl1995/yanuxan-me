package com.msb.pay.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("修改商户DTO")
public class UpdateMchInfoDTO extends MchInfoDTO {

    @NotNull
    @ApiModelProperty("商户主键ID")
    private Long mchPrimaryId;

}
