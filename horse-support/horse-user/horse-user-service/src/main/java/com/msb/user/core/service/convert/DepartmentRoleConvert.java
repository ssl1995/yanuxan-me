package com.msb.user.core.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.user.core.model.entity.DepartmentRole;
import com.msb.user.core.model.vo.DepartmentRoleVO;
import com.msb.user.core.model.dto.DepartmentRoleDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 部门角色表(DepartmentRole)表服务接口
 *
 * @author makejava
 * @date 2022-05-09 10:18:10
 */
@Mapper(componentModel = "spring")
public interface DepartmentRoleConvert {

    DepartmentRoleVO toVo(DepartmentRole departmentRole);

    List<DepartmentRoleVO> toVo(List<DepartmentRole> departmentRole);

    Page<DepartmentRoleVO> toVo(Page<DepartmentRole> departmentRole);

    DepartmentRole toDo(DepartmentRoleDTO departmentRoleDTO);
}

