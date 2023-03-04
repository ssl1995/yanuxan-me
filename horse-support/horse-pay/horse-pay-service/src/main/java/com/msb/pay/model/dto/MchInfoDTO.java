package com.msb.pay.model.dto;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.pay.enums.DisabledEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("新增商户DTO")
public class MchInfoDTO extends BaseMchDataDTO {

    @NotBlank
    @Length(max = 64)
    @ApiModelProperty("商户代号")
    private String mchCode;

    @NotBlank
    @Length(max = 64)
    @ApiModelProperty("商户名称")
    private String mchName;

    @NotBlank
    @Length(max = 64)
    @ApiModelProperty("商户ID")
    private String mchId;

    @NotNull
    @ApiModelPropertyEnum(dictEnum = DisabledEnum.class)
    @ApiModelProperty("商户状态")
    private Boolean isDisabled;

}
