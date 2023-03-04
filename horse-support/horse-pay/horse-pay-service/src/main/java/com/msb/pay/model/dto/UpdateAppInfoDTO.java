package com.msb.pay.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("修改应用信息DTO")
public class UpdateAppInfoDTO extends AppInfoDTO {

    @ApiModelProperty(value = "应用主键ID")
    private Long appPrimaryId;

}
