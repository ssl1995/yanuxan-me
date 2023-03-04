package com.msb.user.core.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.user.core.mapper.EmployeeDepartmentMapper;
import com.msb.user.core.model.entity.EmployeeDepartment;
import com.msb.user.core.model.vo.EmployeeDepartmentVO;
import com.msb.user.core.model.dto.EmployeeDepartmentDTO;
import com.msb.user.core.service.convert.EmployeeDepartmentConvert;
import org.springframework.stereotype.Service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.msb.framework.common.model.PageDTO;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 员工部门表(EmployeeDepartment)表服务实现类
 *
 * @author makejava
 * @date 2022-05-09 14:03:23
 */
@Service("employeeDepartmentService")
public class EmployeeDepartmentService extends ServiceImpl<EmployeeDepartmentMapper, EmployeeDepartment> {

    @Resource
    private EmployeeDepartmentConvert employeeDepartmentConvert;

    @Resource
    private DepartmentRoleService departmentRoleService;

    @Resource
    private EmployeeRoleService employeeRoleService;

    public IPage<EmployeeDepartmentVO> page(PageDTO pageDTO, EmployeeDepartmentDTO employeeDepartmentDTO) {
        return employeeDepartmentConvert.toVo(this.page(pageDTO.page(), new QueryWrapper<EmployeeDepartment>().setEntity(employeeDepartmentConvert.toDo(employeeDepartmentDTO))));
    }

    public EmployeeDepartment getEmployeeDepartment(Long employeeId) {
        return this.lambdaQuery().eq(EmployeeDepartment::getEmployeeId, employeeId).one();
    }

    public List<Long> listEmployeeId(Long departmentId) {
        List<EmployeeDepartment> list = this.lambdaQuery().eq(EmployeeDepartment::getDepartmentId, departmentId).list();
        return list.stream().map(EmployeeDepartment::getEmployeeId).collect(Collectors.toList());
    }

    public EmployeeDepartmentVO getOne(Serializable id) {
        return employeeDepartmentConvert.toVo(this.getById(id));
    }

    public Boolean save(EmployeeDepartmentDTO employeeDepartmentDTO) {
        return this.save(employeeDepartmentConvert.toDo(employeeDepartmentDTO));
    }

    public Boolean update(EmployeeDepartmentDTO employeeDepartmentDTO) {
        return this.updateById(employeeDepartmentConvert.toDo(employeeDepartmentDTO));
    }

    public Boolean delete(List<Long> idList) {
        return this.removeByIds(idList);
    }
}

