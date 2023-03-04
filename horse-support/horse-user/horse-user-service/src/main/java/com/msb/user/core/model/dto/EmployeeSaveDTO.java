package com.msb.user.core.model.dto;


import com.msb.framework.common.model.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * (Employee)表实体类
 *
 * @author makejava
 * @since 2022-03-23 20:13:02
 */
@Data
public class EmployeeSaveDTO implements Serializable {

    @NotNull
    @ApiModelProperty("用户名")
    private String userName;

    @NotBlank
    @ApiModelProperty("手机号")
    private String phone;

    @NotBlank
    @ApiModelProperty("员工姓名")
    private String employeeName;

    @ApiModelProperty("员工类型")
    private Integer employeeType;

    @NotBlank
    @ApiModelProperty("密码")
    private String password;

    @NotBlank
    @ApiModelProperty("邮箱")
    private String email;

    @NotNull
    @ApiModelProperty("是否启用")
    private Boolean isEnable;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("部门id")
    private Long departmentId;
}

