package com.msb.user.core.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.user.core.mapper.EmployeeRoleMapper;
import com.msb.user.core.model.entity.EmployeeRole;
import com.msb.user.core.model.entity.Role;
import com.msb.user.core.model.vo.EmployeeRoleVO;
import com.msb.user.core.model.dto.EmployeeRoleDTO;
import com.msb.user.core.model.vo.RoleVO;
import com.msb.user.core.service.convert.EmployeeRoleConvert;
import org.springframework.stereotype.Service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.msb.framework.common.model.PageDTO;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 系统角色权限表(EmployeeRole)表服务实现类
 *
 * @author makejava
 * @date 2022-05-09 10:18:10
 */
@Service("employeeRoleService")
public class EmployeeRoleService extends ServiceImpl<EmployeeRoleMapper, EmployeeRole> {

    @Resource
    private EmployeeRoleConvert employeeRoleConvert;

    public IPage<EmployeeRoleVO> page(PageDTO pageDTO, EmployeeRoleDTO employeeRoleDTO) {
        return employeeRoleConvert.toVo(this.page(pageDTO.page(), new QueryWrapper<EmployeeRole>().setEntity(employeeRoleConvert.toDo(employeeRoleDTO))));
    }

    public void setRoleToEmployee(Long employeeId, Long... roleIds) {
        setRoleToEmployee(employeeId, Arrays.asList(roleIds));
    }

    public void setRoleToEmployee(Long employeeId, List<Long> roleIds) {
        List<EmployeeRole> employeeRoleList = roleIds.stream().map(roleId -> new EmployeeRole().setEmployeeId(employeeId).setRoleId(roleId)).collect(Collectors.toList());
        this.saveBatch(employeeRoleList);
    }

    public List<Long> listRole(Long employeeId) {
        List<EmployeeRole> list = this.lambdaQuery().eq(EmployeeRole::getEmployeeId, employeeId).list();
        return list.stream().map(EmployeeRole::getRoleId).collect(Collectors.toList());
    }

    public List<Long> listEmployee(Long roleIdList) {
        return this.lambdaQuery().eq(EmployeeRole::getRoleId, roleIdList).list().stream().map(EmployeeRole::getEmployeeId).collect(Collectors.toList());
    }

    public void deleteEmployeeRole(Long employeeId, List<Long> roleId) {
        this.lambdaUpdate().eq(EmployeeRole::getEmployeeId, employeeId).in(EmployeeRole::getRoleId, roleId).remove();
    }

    public void deleteEmployeeAllRole(Long employeeId) {
        this.lambdaUpdate().eq(EmployeeRole::getEmployeeId, employeeId).remove();
    }

    public EmployeeRoleVO getOne(Serializable id) {
        return employeeRoleConvert.toVo(this.getById(id));
    }

    public Boolean save(EmployeeRoleDTO employeeRoleDTO) {
        return this.save(employeeRoleConvert.toDo(employeeRoleDTO));
    }

    public Boolean update(EmployeeRoleDTO employeeRoleDTO) {
        return this.updateById(employeeRoleConvert.toDo(employeeRoleDTO));
    }

    public Boolean delete(List<Long> idList) {
        return this.removeByIds(idList);
    }
}

