package com.msb.user.core.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.msb.framework.mysql.BaseEntity;

import lombok.experimental.Accessors;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 系统角色表(Role)表实体类
 *
 * @author makejava
 * @date 2022-05-09 10:18:11
 */
@Accessors(chain = true)
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("role")
public class Role extends BaseEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色描述
     */
    private String roleDesc;

    /**
     * 角色所属系统
     */
    private Long systemId;

    /**
     * 是否开启
     */
    private Boolean isEnable;


    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;

}
