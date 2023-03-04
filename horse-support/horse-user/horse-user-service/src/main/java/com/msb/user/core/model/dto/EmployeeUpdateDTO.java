package com.msb.user.core.model.dto;


import com.msb.framework.common.model.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * (Employee)表实体类
 *
 * @author makejava
 * @since 2022-03-23 20:13:02
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EmployeeUpdateDTO implements Serializable {


    @ApiModelProperty("员工id")
    private Long id;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("员工姓名")
    private String employeeName;

    @ApiModelProperty("员工类型")
    private Integer employeeType;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("是否启用")
    private Boolean isEnable;

    @ApiModelProperty("备注")
    private String remark;

}

