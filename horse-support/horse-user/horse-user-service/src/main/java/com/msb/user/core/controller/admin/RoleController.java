package com.msb.user.core.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.msb.framework.web.transform.annotation.Transform;
import com.msb.user.auth.AuthAdmin;
import com.msb.user.core.manager.RoleManager;
import com.msb.user.core.model.dto.RoleDTO;
import com.msb.user.core.model.dto.RoleMenuPermissionDTO;
import com.msb.user.core.model.dto.RoleQueryDTO;
import com.msb.user.core.model.vo.RoleMenuPermissionRelationVO;
import com.msb.user.core.model.vo.RolePageVO;
import com.msb.user.core.model.vo.RoleVO;
import com.msb.user.core.model.vo.SystemGroupRoleVO;
import com.msb.user.core.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统角色表(Role)表控制层
 *
 * @author makejava
 * @date 2022-05-09 10:18:11
 */
@Api(tags = "（后管）角色相关接口")
@AuthAdmin
@RestController
@RequestMapping("role")
public class RoleController {
    /**
     * 服务对象
     */
    @Resource
    private RoleService roleService;

    @Resource
    private RoleManager roleManager;

    @Transform
    @ApiOperation("分页查询角色")
    @GetMapping
    public IPage<RolePageVO> page(RoleQueryDTO roleQueryDTO) {
        return roleService.page(roleQueryDTO);
    }

    @ApiOperation("查询部门下的角色 分组")
    @GetMapping("department")
    public List<SystemGroupRoleVO> getDepartmentRole(Long departmentId) {
        return roleService.listSystemGroupRole(departmentId);
    }

    @ApiOperation("获取角色的菜单和接口 权限属性结构")
    @GetMapping("{id}")
    public RoleMenuPermissionRelationVO getRoleMenuPermissionRelationVO(@PathVariable Long id) {
        return this.roleManager.getRoleMenuPermissionRelationVO(id);
    }

    @ApiOperation("保存角色信息")
    @PostMapping
    public RoleVO save(@RequestBody RoleDTO roleDTO) {
        return this.roleManager.saveRoleMenuPermission(roleDTO);
    }

    @ApiOperation("更新角色信息")
    @PutMapping("{id}")
    public Boolean update(@PathVariable Long id, @RequestBody RoleDTO roleDTO) {
        return this.roleManager.updateRoleMenuPermission(id, roleDTO);
    }

    @ApiOperation("设置角色的菜单与接口权限")
    @PutMapping("menu")
    public Boolean setRoleMenuPermission(@RequestBody RoleMenuPermissionDTO roleMenuPermissionDTO) {
        roleManager.setMenuPermissionToRole(roleMenuPermissionDTO.getId(), roleMenuPermissionDTO.getMenuIds(), roleMenuPermissionDTO.getPermissionIds());
        return Boolean.TRUE;
    }

    @ApiOperation("删除角色信息")
    @DeleteMapping
    public Boolean delete(@RequestParam("idList") List<Long> idList) {
        return this.roleService.delete(idList);
    }
}

