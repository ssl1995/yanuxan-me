package com.msb.mall.base.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.msb.framework.mysql.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * (ReceiveAddress)表实体类
 *
 * @author makejava
 * @date 2022-03-31 13:57:14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("receive_address")
public class ReceiveAddress extends BaseEntity implements Serializable {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 收货人姓名
     */
    private String name;

    /**
     * 收货人手机
     */
    private String phone;

    /**
     * 省区域代码
     */
    private String provinceCode;

    /**
     * 省
     */
    private String province;

    /**
     * 市区域代码
     */
    private String cityCode;

    /**
     * 市
     */
    private String city;

    /**
     * 县区域代码
     */
    private String areaCode;

    /**
     * 县
     */
    private String area;

    /**
     * 详细地址
     */
    private String detailAddress;

    /**
     * 是否默认
     */
    private Boolean isDefault;


    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;

}
