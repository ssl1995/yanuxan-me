package com.msb.im.module.waiter.model.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 店铺配置
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Data
@ApiModel("新增店铺DTO")
public class AddStoreConfigDTO {
    @ApiModelProperty("系统id")
    @NotNull(message = "系统不能为空")
    private Integer sysId;
    @ApiModelProperty("店铺名称")
    @NotBlank(message = "店铺名称不能为空")
    private String name;
    @ApiModelProperty("店铺头像")
    @NotBlank(message = "店铺头像不能为空")
    private String avatar;
}
