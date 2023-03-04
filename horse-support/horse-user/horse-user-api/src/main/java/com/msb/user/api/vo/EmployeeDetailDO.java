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
public class EmployeeDetailDO implements Serializable {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("员工用户信息")
    private UserDO userVO;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户id")
    private String userName;

    @ApiModelProperty("员工姓名")
    private String employeeName;

    @ApiModelProperty("员工类型")
    private Integer employeeType;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("员工类型")
    private Long status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;
}

