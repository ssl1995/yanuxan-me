package com.msb.user.core.model.dto;


import com.msb.framework.common.model.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * (Employee)表实体类
 *
 * @author makejava
 * @since 2022-03-23 20:13:02
 */
@Data
public class EmployeeQueryDTO extends PageDTO implements Serializable {

    @ApiModelProperty("用户名，姓名")
    private String userName;

    @ApiModelProperty("部门id")
    private Long departmentId;

    @ApiModelProperty("手机号")
    private String phone;

}

