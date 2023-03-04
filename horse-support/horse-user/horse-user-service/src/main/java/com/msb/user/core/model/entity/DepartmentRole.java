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
 * 部门角色表(DepartmentRole)表实体类
 *
 * @author makejava
 * @date 2022-05-09 10:18:10
 */
@Accessors(chain = true)
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("department_role")
public class DepartmentRole extends BaseEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 部门ID
     */
    private Long departmentId;

    /**
     * 系统ID
     */
    private Long systemId;


    /**
     * 逻辑删除
     */
    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;

}
