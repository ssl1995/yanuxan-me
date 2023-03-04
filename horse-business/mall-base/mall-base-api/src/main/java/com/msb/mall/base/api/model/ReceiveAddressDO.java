package com.msb.mall.base.api.model;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * (ReceiveAddress)表实体类
 *
 * @author makejava
 * @date 2022-03-31 13:57:15
 */
@Data
public class ReceiveAddressDO implements Serializable {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("收货人姓名")
    private String name;

    @ApiModelProperty("收货人手机")
    private String phone;

    @ApiModelProperty("省区域代码")
    private String provinceCode;

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("市区域代码")
    private String cityCode;

    @ApiModelProperty("市")
    private String city;

    @ApiModelProperty("县区域代码")
    private String areaCode;

    @ApiModelProperty("县")
    private String area;

    @ApiModelProperty("详细地址")
    private String detailAddress;

    @ApiModelProperty("是否默认")
    private Boolean isDefault;

    @ApiModelProperty("是否偏远地区")
    private Boolean isRemoteArea;

    public ReceiveAddressDO() {
        this.isRemoteArea = false;
    }
}

