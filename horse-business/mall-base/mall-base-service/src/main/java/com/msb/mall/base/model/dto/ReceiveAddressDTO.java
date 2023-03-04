package com.msb.mall.base.model.dto;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;


/**
 * (ReceiveAddress)表实体类
 *
 * @author makejava
 * @date 2022-03-31 13:57:15
 */
@Data
public class ReceiveAddressDTO implements Serializable {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @Length(max = 8)
    @NotNull
    @ApiModelProperty("收货人姓名")
    private String name;

    @NotNull
    @ApiModelProperty("收货人手机")
    private String phone;

    @NotNull
    @ApiModelProperty("省区域代码")
    private String provinceCode;

    @NotNull
    @ApiModelProperty("省")
    private String province;

    @NotNull
    @ApiModelProperty("市区域代码")
    private String cityCode;

    @NotNull
    @ApiModelProperty("市")
    private String city;

    @NotNull
    @ApiModelProperty("县区域代码")
    private String areaCode;

    @NotNull
    @ApiModelProperty("县")
    private String area;

    @Length(max = 30)
    @NotNull
    @ApiModelProperty("详细地址")
    private String detailAddress;

    @NotNull
    @ApiModelProperty("是否默认")
    private Boolean isDefault;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;
}

