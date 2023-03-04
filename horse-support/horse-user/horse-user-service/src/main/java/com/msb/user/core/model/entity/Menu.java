package com.msb.user.core.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.msb.framework.mysql.BaseEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * (Menu)表实体类
 *
 * @author makejava
 * @since 2022-03-23 20:13:01
 */
@Accessors(chain = true)
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("menu")
public class Menu extends BaseEntity implements Serializable {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 父级菜单
     */
    private Long parentId;

    /**
     * 系统id
     */
    private Long systemId;

    /**
     * 菜单标题
     */
    private String title;

    /**
     * 菜单权限标识
     */
    private String permission;

    /**
     * 菜单类型
     */
    private Integer type;

    /**
     * 前端页面
     */
    private String page;

    /**
     * 前端路由
     */
    private String path;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 激活菜单
     */
    private String activeMenu;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否开启
     */
    private Boolean isEnable;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;

}

