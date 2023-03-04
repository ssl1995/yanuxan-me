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
 * 系统角色菜单表(RoleMenu)表实体类
 *
 * @author makejava
 * @date 2022-05-09 10:18:12
 */
@Accessors(chain = true)
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("role_menu")
public class RoleMenu extends BaseEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 角色Id
     */
    private Long roleId;

    /**
     * 菜单ID
     */
    private Long menuId;

    /**
     * 系统id
     */
    private Long systemId;


    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;

}
