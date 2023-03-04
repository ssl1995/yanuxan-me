package com.msb.business.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.msb.framework.mysql.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * (HorseUser)表实体类
 *
 * @author makejava
 * @since 2022-03-21 14:57:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("horse_user")
public class HorseUser extends BaseEntity implements Serializable {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 名字
     */
    private String name;

    /**
     * 地址
     */
    private String address;


    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;

}

