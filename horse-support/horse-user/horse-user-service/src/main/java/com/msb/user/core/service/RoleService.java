package com.msb.user.core.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.user.core.mapper.RoleMapper;
import com.msb.user.core.model.dto.RoleDTO;
import com.msb.user.core.model.dto.RoleQueryDTO;
import com.msb.user.core.model.entity.Role;
import com.msb.user.core.model.vo.RolePageVO;
import com.msb.user.core.model.vo.RoleVO;
import com.msb.user.core.model.vo.SystemGroupRoleVO;
import com.msb.user.core.service.convert.RoleConvert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 系统角色表(Role)表服务实现类
 *
 * @author makejava
 * @date 2022-05-09 10:18:12
 */
@Service("roleService")
public class RoleService extends ServiceImpl<RoleMapper, Role> {

    @Resource
    private RoleConvert roleConvert;

    @Resource
    private DepartmentRoleService departmentRoleService;

    @Resource
    private EmployeeRoleService employeeRoleService;

    @Resource
    private SystemService systemService;

    @Resource
    private DepartmentService departmentService;


    public IPage<RolePageVO> page(RoleQueryDTO roleQueryDTO) {
        Page<Role> page = this.lambdaQuery().like(StringUtils.isNotBlank(roleQueryDTO.getRoleName()), Role::getRoleName, roleQueryDTO.getRoleName())
                .eq(Objects.nonNull(roleQueryDTO.getSystemId()), Role::getSystemId, roleQueryDTO.getSystemId())
                .eq(Objects.nonNull(roleQueryDTO.getIsEnable()), Role::getIsEnable, roleQueryDTO.getIsEnable()).page(roleQueryDTO.page());
        Page<RolePageVO> voPage = roleConvert.toPageVo(page);
        voPage.getRecords().forEach(rolePageVO -> {
            List<Long> departmentIdList = departmentRoleService.listDepartmentId(rolePageVO.getId());
            rolePageVO.setDepartmentList(departmentService.listDepartmentVO(departmentIdList));
        });
        return voPage;
    }

    private List<RoleVO> listRole(List<Long> id) {
        if (id.isEmpty()) {
            return Collections.emptyList();
        }
        List<Role> list = this.lambdaQuery().in(Role::getId, id).orderByAsc(Role::getSystemId).list();
        return roleConvert.toVo(list);
    }

    public List<RoleVO> listRole(Long departmentId) {
        List<Long> roleIds = departmentRoleService.listRoleId(departmentId);
        return listRole(roleIds);
    }

    public List<RoleVO> listEmployeeRole(Long employeeId) {
        List<Long> roleIds = employeeRoleService.listRole(employeeId);
        return listRole(roleIds);
    }

    /**
     * 查询部门下的角色 根据系统分组了
     */
    public List<SystemGroupRoleVO> listSystemGroupRole(Long departmentId) {
        List<Long> roleIds = departmentRoleService.listRoleId(departmentId);
        List<RoleVO> role = listRole(roleIds);

        Map<Long, List<RoleVO>> systemRoleMap = role.stream().collect(Collectors.groupingBy(RoleVO::getSystemId, Collectors.toList()));

        return systemRoleMap.keySet().stream().map(systemId -> new SystemGroupRoleVO()
                .setSystemVO(systemService.getOne(systemId))
                .setRoleListVO(systemRoleMap.get(systemId))).collect(Collectors.toList());
    }

    public Boolean update(RoleDTO roleDTO) {
        return this.updateById(roleConvert.toDo(roleDTO));
    }

    public Boolean delete(List<Long> idList) {
        return this.removeByIds(idList);
    }
}

