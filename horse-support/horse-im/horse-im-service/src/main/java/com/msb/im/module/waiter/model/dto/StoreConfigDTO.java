package com.msb.im.module.waiter.model.dto;


import com.msb.framework.common.model.PageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 店铺配置
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("店铺配置DTO")
public class StoreConfigDTO extends PageDTO {
    @ApiModelProperty("所属系统id")
    private Integer sysId;
    @ApiModelProperty("店铺名称")
    private String name;
}
