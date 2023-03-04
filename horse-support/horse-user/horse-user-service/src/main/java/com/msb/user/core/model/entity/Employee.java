package com.msb.user.core.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.msb.framework.mysql.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * (Employee)表实体类
 *
 * @author makejava
 * @since 2022-03-23 20:13:01
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("employee")
public class Employee extends BaseEntity implements Serializable {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户id
     */
    private String userName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 员工姓名
     */
    private String employeeName;

    /**
     * 员工类型
     */
    private Integer employeeType;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean isEnable;


    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;

}

