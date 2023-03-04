package com.msb.user.core.model.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 部门表(Department)表实体类
 *
 * @author makejava
 * @date 2022-05-09 10:25:50
 */
@Data
@Accessors(chain = true)
public class DepartmentTreeVO implements Serializable {

    private Long id;

    @ApiModelProperty("部门名称")
    private String name;

    @ApiModelProperty("部门链")
    private String chainId;

    @ApiModelProperty("上级部门ID")
    private Long parentId;

    @ApiModelProperty("0:可用 1：删除")
    private Boolean isEnable;

    @ApiModelProperty("下级部门")
    private List<DepartmentTreeVO> childDepartment;
}

