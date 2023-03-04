package com.msb.user.core.manager;

import com.msb.framework.common.context.UserContext;
import com.msb.user.core.model.dto.RoleDTO;
import com.msb.user.core.model.dto.RoleMenuPermissionDTO;
import com.msb.user.core.model.dto.SetRoleToDepartmentDTO;
import com.msb.user.core.model.entity.Role;
import com.msb.user.core.model.entity.RoleMenu;
import com.msb.user.core.model.entity.RolePermission;
import com.msb.user.core.model.vo.*;
import com.msb.user.core.service.*;
import com.msb.user.core.service.convert.RoleConvert;
import org.springframework.data.redis.connection.convert.ListConverter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class RoleManager {

    @Resource
    private RoleService roleService;

    @Resource
    private RoleMenuService roleMenuService;

    @Resource
    private RolePermissionService rolePermissionService;

    @Resource
    private MenuService menuService;

    @Resource
    private PermissionService permissionService;

    @Resource
    private RoleConvert roleConvert;

    @Resource
    private EmployeeRoleService employeeRoleService;

    @Resource
    private DepartmentRoleService departmentRoleService;

    /**
     * 查询当前用户的拥有的菜单
     */
    public List<MenuTreeVO> listEmployeeMenuTree() {
        Integer systemId = UserContext.get().getSystemId();
        List<RoleVO> roleList = roleService.listEmployeeRole(UserContext.get().getEmployeeId());
        List<Long> roleIdList = roleList.stream().map(RoleVO::getId).collect(Collectors.toList());
        Set<Long> menuIdList = roleMenuService.listRoleMenuId(roleIdList);

        List<MenuTreeVO> menuTreeListVO = menuService.listMenuTreeVO(systemId)
                .stream()
                .filter(menuTreeVO -> menuIdList.contains(menuTreeVO.getId()))
                .collect(Collectors.toList());
        return menuService.buildMenuTree(menuTreeListVO, MenuTreeVO.class);
    }

    /**
     * 获取角色的菜单和接口 权限属性结构（后管）
     */
    public RoleMenuPermissionRelationVO getRoleMenuPermissionRelationVO(Long id) {
        Role role = roleService.getById(id);
        List<RoleMenu> roleMenus = roleMenuService.listRoleMenu(role.getId());
        List<RolePermission> rolePermissions = rolePermissionService.listRolePermission(role.getId());

        Set<Long> menuIds = roleMenus.stream().map(RoleMenu::getMenuId).collect(Collectors.toSet());
        Set<Long> permissionIds = rolePermissions.stream().map(RolePermission::getPermissionId).collect(Collectors.toSet());

        List<RoleMenuTreeVO> roleMenuTreeList = menuService.listRoleMenuTreeVO(role.getSystemId());

        roleMenuTreeList.forEach(roleMenuTreeVO -> {
            if (menuIds.contains(roleMenuTreeVO.getId())) {
                roleMenuTreeVO.setIsHave(Boolean.TRUE);
            }
            //拼装接口权限
            List<RolePermissionRelationVO> rolePermissionRelationListVO = permissionService.listRolePermissionRelation(roleMenuTreeVO.getId());
            rolePermissionRelationListVO.forEach(rolePermissionRelationVO ->
                    rolePermissionRelationVO.setIsHave(permissionIds.contains(rolePermissionRelationVO.getId())));
            roleMenuTreeVO.setPermissionChild(rolePermissionRelationListVO);
        });

        List<RoleMenuTreeVO> roleMenuTreeListVO = menuService.buildMenuTree(roleMenuTreeList, RoleMenuTreeVO.class);

        return new RoleMenuPermissionRelationVO()
                .setRole(roleConvert.toVo(role))
                .setRoleMenuTree(roleMenuTreeListVO);
    }

    /**
     * 保存权限和菜单
     */
    @Transactional(rollbackFor = Exception.class)
    public RoleVO saveRoleMenuPermission(RoleDTO roleDTO) {
        Role role = roleConvert.toDo(roleDTO);
        roleService.save(role);
        return roleConvert.toVo(role);
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean updateRoleMenuPermission(Long id, RoleDTO roleDTO) {
        Role role = roleConvert.toDo(roleDTO).setId(id);
        roleService.updateById(role);
        return Boolean.TRUE;
    }

    @Transactional(rollbackFor = Exception.class)
    public void clearRoleAllMenuPermission(Long roleId) {
        roleMenuService.delete(roleId);
        rolePermissionService.delete(roleId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void setMenuPermissionToRole(Long roleId, Long[] menuIds, Long[] permissionIds) {
        Role role = roleService.getById(roleId);

        Set<Long> oldMenuIdList = roleMenuService.listRoleMenu(roleId).stream().map(RoleMenu::getMenuId).collect(Collectors.toSet());
        Set<Long> newMenuIdList = Arrays.stream(menuIds).collect(Collectors.toSet());

        //需要删除菜单id
        List<Long> delMenuIdList = oldMenuIdList.stream().filter(id -> !newMenuIdList.contains(id)).collect(Collectors.toList());

        //需要增加的菜单
        List<RoleMenu> addRoleMenuList = newMenuIdList.stream().filter(id -> !oldMenuIdList.contains(id)).map(menuId -> new RoleMenu()
                .setMenuId(menuId)
                .setRoleId(role.getId())
                .setSystemId(role.getSystemId())).collect(Collectors.toList());

        roleMenuService.delete(roleId, delMenuIdList);
        roleMenuService.saveBatch(addRoleMenuList);

        //逻辑同上
        Set<Long> oldPermissionIdList = rolePermissionService.listRolePermission(roleId).stream().map(RolePermission::getPermissionId).collect(Collectors.toSet());
        Set<Long> newPermissionIdList = Arrays.stream(permissionIds).collect(Collectors.toSet());

        List<Long> delPermissionIdList = oldPermissionIdList.stream().filter(id -> !newPermissionIdList.contains(id)).collect(Collectors.toList());
        rolePermissionService.delete(roleId, delPermissionIdList);

        List<RolePermission> rolePermissionList = newPermissionIdList.stream().filter(id -> !oldPermissionIdList.contains(id)).map(permissionId -> new RolePermission()
                .setPermissionId(permissionId)
                .setRoleId(role.getId())
                .setSystemId(role.getSystemId())).collect(Collectors.toList());

        rolePermissionService.saveBatch(rolePermissionList);
    }

    public void setRoleToEmployee(Long employeeId, Long[] roleIds) {
        employeeRoleService.deleteEmployeeAllRole(employeeId);
        employeeRoleService.setRoleToEmployee(employeeId, roleIds);
    }
}
