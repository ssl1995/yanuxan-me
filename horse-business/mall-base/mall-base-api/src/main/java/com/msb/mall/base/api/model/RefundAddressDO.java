package com.msb.mall.base.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class RefundAddressDO implements Serializable {

    @ApiModelProperty("收货人姓名")
    private String receiveName;

    @ApiModelProperty("收货人姓名")
    private String receivePhone;

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
}
