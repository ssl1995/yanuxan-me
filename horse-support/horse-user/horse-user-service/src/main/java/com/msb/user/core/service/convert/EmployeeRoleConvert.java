package com.msb.user.core.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.user.core.model.entity.EmployeeRole;
import com.msb.user.core.model.vo.EmployeeRoleVO;
import com.msb.user.core.model.dto.EmployeeRoleDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 系统角色权限表(EmployeeRole)表服务接口
 *
 * @author makejava
 * @date 2022-05-09 10:18:10
 */
@Mapper(componentModel = "spring")
public interface EmployeeRoleConvert {

    EmployeeRoleVO toVo(EmployeeRole employeeRole);

    List<EmployeeRoleVO> toVo(List<EmployeeRole> employeeRole);

    Page<EmployeeRoleVO> toVo(Page<EmployeeRole> employeeRole);

    EmployeeRole toDo(EmployeeRoleDTO employeeRoleDTO);
}

