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
 * 部门表(Department)表实体类
 *
 * @author makejava
 * @date 2022-05-09 11:16:28
 */
@Accessors(chain = true)
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("department")
public class Department extends BaseEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 部门名称
     */
    private String name;

    /**
     * 部门链
     */
    private String chainId;

    /**
     * 上级部门ID
     */
    private Long parentId;

    /**
     * 1:一级 2：二级 3：三级 4：四级
     */
    private Integer level;

    /**
     * 0:可用 1：删除
     */
    private Boolean isEnable;

    /**
     * 逻辑删除
     */
    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;

}
