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
 * 员工部门表(EmployeeDepartment)表实体类
 *
 * @author makejava
 * @date 2022-05-09 14:03:23
 */
@Accessors(chain = true)
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("employee_department")
public class EmployeeDepartment extends BaseEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 员工Id
     */
    private Long employeeId;

    /**
     * 部门id
     */
    private Long departmentId;


    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;

}
