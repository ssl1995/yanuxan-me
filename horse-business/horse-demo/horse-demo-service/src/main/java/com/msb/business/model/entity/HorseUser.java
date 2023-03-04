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
 * @since 2022-03-16 11:12:35
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("horse_user")
public class HorseUser extends BaseEntity implements Serializable {

    @TableId
    private Long id;

    private String name;

    private String address;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;
}

