package com.msb.user.core.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.common.model.PageDTO;
import com.msb.user.core.mapper.DepartmentRoleMapper;
import com.msb.user.core.model.dto.DelRoleOfDepartmentDTO;
import com.msb.user.core.model.dto.DepartmentRoleDTO;
import com.msb.user.core.model.dto.SetRoleToDepartmentDTO;
import com.msb.user.core.model.entity.DepartmentRole;
import com.msb.user.core.model.vo.DepartmentRoleVO;
import com.msb.user.core.service.convert.DepartmentRoleConvert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 部门角色表(DepartmentRole)表服务实现类
 *
 * @author makejava
 * @date 2022-05-09 10:18:10
 */
@Service("departmentRoleService")
public class DepartmentRoleService extends ServiceImpl<DepartmentRoleMapper, DepartmentRole> {

    @Resource
    private DepartmentRoleConvert departmentRoleConvert;

    @Resource
    private EmployeeRoleService employeeRoleService;

    @Resource
    private EmployeeDepartmentService employeeDepartmentService;

    @Resource
    private RoleService roleService;

    public IPage<DepartmentRoleVO> page(PageDTO pageDTO, DepartmentRoleDTO departmentRoleDTO) {
        return departmentRoleConvert.toVo(this.page(pageDTO.page(), new QueryWrapper<DepartmentRole>().setEntity(departmentRoleConvert.toDo(departmentRoleDTO))));
    }

    public DepartmentRoleVO getOne(Serializable id) {
        return departmentRoleConvert.toVo(this.getById(id));
    }

    public List<Long> listRoleId(Long departmentId) {
        List<DepartmentRole> list = this.lambdaQuery().eq(DepartmentRole::getDepartmentId, departmentId).list();
        return list.stream().map(DepartmentRole::getRoleId).collect(Collectors.toList());
    }

    public List<Long> listDepartmentId(Long roleId) {
        List<DepartmentRole> list = this.lambdaQuery().eq(DepartmentRole::getRoleId, roleId).list();
        return list.stream().map(DepartmentRole::getDepartmentId).collect(Collectors.toList());
    }

    /**
     * 设置这个角色到这个部门， 看是否需要把权限分配给这个部门下的员工
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean setRoleToDepartment(Long[] roleIds, Long departmentId, Boolean isDistributionCurrentDepartmentEmployee) {
        List<DepartmentRole> departmentRoleList = Arrays.stream(roleIds)
                .map(roleId -> {
                    Optional<DepartmentRole> optionalDepartmentRole = this.lambdaQuery().eq(DepartmentRole::getRoleId, roleId).eq(DepartmentRole::getDepartmentId, departmentId).oneOpt();
                    if (optionalDepartmentRole.isPresent()) {
                        return null;
                    }
                    return new DepartmentRole()
                            .setRoleId(roleId)
                            .setSystemId(roleService.getById(roleId).getSystemId())
                            .setDepartmentId(departmentId);
                }).filter(Objects::nonNull)
                .collect(Collectors.toList());
        this.saveBatch(departmentRoleList);

        if (isDistributionCurrentDepartmentEmployee) {
            List<Long> employeeIdList = employeeDepartmentService.listEmployeeId(departmentId);
            employeeIdList.forEach(employeeId -> employeeRoleService.setRoleToEmployee(employeeId, roleIds));
        }
        return Boolean.TRUE;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean setRoleToDepartment(SetRoleToDepartmentDTO setRoleToDepartmentDTO) {
        return setRoleToDepartment(setRoleToDepartmentDTO.getRoleIds(),
                setRoleToDepartmentDTO.getDepartmentId(),
                setRoleToDepartmentDTO.getIsDistributionCurrentDepartmentEmployee());
    }

    /**
     * 删除这个部门下的这个角色，是否需要清除这个部门下员工的 这个角色
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean delRoleOfDepartment(DelRoleOfDepartmentDTO delRoleOfDepartmentDTO) {
        Long departmentId = delRoleOfDepartmentDTO.getDepartmentId();
        Long roleId = delRoleOfDepartmentDTO.getRoleId();
        Optional<DepartmentRole> departmentRole = this.lambdaQuery()
                .eq(DepartmentRole::getDepartmentId, departmentId)
                .eq(DepartmentRole::getRoleId, roleId)
                .list()
                .stream()
                .findAny();
        departmentRole.ifPresent(role -> this.removeById(role.getId()));
        if (delRoleOfDepartmentDTO.getIsDeleteDepartmentOfEmployeeRole()) {
            List<Long> employeeIdList = employeeDepartmentService.listEmployeeId(delRoleOfDepartmentDTO.getDepartmentId());
            employeeIdList.forEach(employeeId -> employeeRoleService.deleteEmployeeRole(employeeId, Collections.singletonList(roleId)));
        }
        return Boolean.TRUE;
    }

    public Boolean save(DepartmentRoleDTO departmentRoleDTO) {
        return this.save(departmentRoleConvert.toDo(departmentRoleDTO));
    }

    public Boolean update(DepartmentRoleDTO departmentRoleDTO) {
        return this.updateById(departmentRoleConvert.toDo(departmentRoleDTO));
    }

    public Boolean delete(List<Long> idList) {
        return this.removeByIds(idList);
    }
}

