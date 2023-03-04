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
 * 权限表(Permission)表实体类
 *
 * @author makejava
 * @date 2022-05-09 10:18:11
 */
@Accessors(chain = true)
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("permission")
public class Permission extends BaseEntity implements Serializable {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 系统id
     */
    private Long systemId;

    /**
     * 所属菜单id
     */
    private Long menuId;

    /**
     * 权限类型，1，需要分配，2，登录既有的
     */
    private Integer type;

    /**
     * 服务名
     */
    private String service;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口路径
     */
    private String uri;

    /**
     * http请求类型 GET,POST,PUT,DELETE 等
     */
    private String method;

    /**
     * 是否开启
     */
    private Boolean isEnable;


    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;

}
