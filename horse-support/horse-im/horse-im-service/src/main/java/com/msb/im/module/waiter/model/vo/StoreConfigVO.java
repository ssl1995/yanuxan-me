package com.msb.im.module.waiter.model.vo;


import com.msb.im.model.entity.BaseEntity;
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
@ApiModel("系统店铺配置")
public class StoreConfigVO extends BaseEntity {
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("所属系统id")
    private Integer sysId;
    @ApiModelProperty("店铺名称")
    private String name;
    @ApiModelProperty("店铺头像")
    private String avatar;

}
