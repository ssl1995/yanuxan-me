package com.msb.user.core.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.user.core.model.entity.EmployeeDepartment;
import com.msb.user.core.model.vo.EmployeeDepartmentVO;
import com.msb.user.core.model.dto.EmployeeDepartmentDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 员工部门表(EmployeeDepartment)表服务接口
 *
 * @author makejava
 * @date 2022-05-09 14:03:23
 */
@Mapper(componentModel = "spring")
public interface EmployeeDepartmentConvert {

    EmployeeDepartmentVO toVo(EmployeeDepartment employeeDepartment);

    List<EmployeeDepartmentVO> toVo(List<EmployeeDepartment> employeeDepartment);

    Page<EmployeeDepartmentVO> toVo(Page<EmployeeDepartment> employeeDepartment);

    EmployeeDepartment toDo(EmployeeDepartmentDTO employeeDepartmentDTO);
}

