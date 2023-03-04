package com.msb.pay.model.dto;

import com.msb.framework.common.model.PageDTO;
import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.pay.enums.DisabledEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("商户分页列表DTO")
public class MchInfoPageDTO extends PageDTO {

    @ApiModelProperty("商户代号")
    private String mchCode;

    @ApiModelProperty("商户名称")
    private String mchName;

    @ApiModelProperty("商户ID")
    private String mchId;

    @ApiModelPropertyEnum(dictEnum = DisabledEnum.class)
    @ApiModelProperty("商户状态")
    private Boolean isDisabled;

}
