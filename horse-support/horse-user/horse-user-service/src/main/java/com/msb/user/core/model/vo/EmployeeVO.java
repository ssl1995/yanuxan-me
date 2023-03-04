package com.msb.user.core.model.vo;


import com.msb.framework.web.transform.annotation.Transform;
import com.msb.user.core.translate.CreateUpdateUserTransformer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


/**
 * (Employee)表实体类
 *
 * @author makejava
 * @since 2022-03-23 20:13:01
 */
@Data
public class EmployeeVO implements Serializable {

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

    @Transform(transformer = CreateUpdateUserTransformer.class, from = "createUser")
    private String createUserName;

    private Long updateUser;

    @Transform(transformer = CreateUpdateUserTransformer.class, from = "updateUser")
    private String updateUserName;

    @ApiModelProperty("部门链名称")
    private String departmentNameChain;

    @ApiModelProperty("角色列表")
    private List<RoleVO> roleList;
}

