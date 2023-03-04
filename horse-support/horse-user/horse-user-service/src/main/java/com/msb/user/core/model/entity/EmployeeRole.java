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
 * 系统角色权限表(EmployeeRole)表实体类
 *
 * @author makejava
 * @date 2022-05-09 10:18:10
 */
@Accessors(chain = true)
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("employee_role")
public class EmployeeRole extends BaseEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 员工Id
     */
    private Long employeeId;

    /**
     * 角色Id
     */
    private Long roleId;


    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;

}
