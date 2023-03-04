package com.msb.user.api.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * (Employee)表实体类
 *
 * @author makejava
 * @since 2022-03-23 20:13:01
 */
@Data
public class EmployeeDO implements Serializable {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户id")
    private String userName;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("员工姓名")
    private String employeeName;

    @ApiModelProperty("员工类型")
    private Integer employeeType;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("是否启用")
    private Boolean isEnable;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;
}

