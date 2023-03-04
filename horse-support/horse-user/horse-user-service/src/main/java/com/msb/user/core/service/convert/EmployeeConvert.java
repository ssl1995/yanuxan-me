package com.msb.user.core.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.framework.common.model.UserLoginInfo;
import com.msb.user.api.vo.EmployeeDO;
import com.msb.user.core.model.dto.EmployeeQueryDTO;
import com.msb.user.core.model.dto.EmployeeSaveDTO;
import com.msb.user.core.model.dto.EmployeeUpdateDTO;
import com.msb.user.core.model.entity.Employee;
import com.msb.user.core.model.vo.EmployeeVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * (Employee)表服务接口
 *
 * @author makejava
 * @since 2022-03-23 20:13:01
 */
@Mapper(componentModel = "spring")
public interface EmployeeConvert {

    EmployeeVO toVo(Employee employee);

    List<EmployeeVO> toVo(List<Employee> employee);

    Page<EmployeeVO> toVo(Page<Employee> employee);

    List<EmployeeDO> toDo(List<Employee> employee);

    Page<EmployeeDO> toDo(Page<Employee> employee);

    EmployeeDO toDo(Employee employee);

    Employee toDo(EmployeeSaveDTO employeeDTO);

    Employee toDo(EmployeeQueryDTO employeeDTO);

    Employee toDo(EmployeeUpdateDTO employeeDTO);
}

