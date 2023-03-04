package com.msb.user.core.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.msb.framework.common.context.UserContext;
import com.msb.framework.web.transform.annotation.Transform;
import com.msb.user.api.vo.EmployeeDO;
import com.msb.user.auth.AuthAdmin;
import com.msb.user.core.manager.DepartmentManager;
import com.msb.user.core.manager.EmployeeManager;
import com.msb.user.core.manager.RoleManager;
import com.msb.user.core.model.dto.*;
import com.msb.user.core.model.vo.EmployeeVO;
import com.msb.user.core.model.vo.RoleVO;
import com.msb.user.core.service.EmployeeService;
import com.msb.user.core.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * (Employee)表控制层
 *
 * @author makejava
 * @since 2022-03-23 20:13:01
 */
@Api(tags = "（B端后管访问）员工B端用户相关接口")
@RestController
@RequestMapping("employee")
@AuthAdmin
public class EmployeeController {
    /**
     * 服务对象
     */
    @Resource
    private EmployeeService employeeService;

    @Resource
    private RoleService roleService;

    @Resource
    private RoleManager roleManager;

    @Resource
    private DepartmentManager departmentManager;

    @Resource
    private EmployeeManager employeeManager;

    @Transform
    @ApiOperation("分页查询员工列表")
    @GetMapping
    public IPage<EmployeeVO> page(EmployeeQueryDTO employeeDTO) {
        return employeeService.page(employeeDTO);
    }

    @Transform
    @ApiOperation("获取员工权限")
    @GetMapping("role/{employeeId}")
    public List<RoleVO> listEmployeeRole(@PathVariable Long employeeId) {
        return roleService.listEmployeeRole(employeeId);
    }

    @ApiOperation("设置员工角色")
    @PutMapping("role/{employeeId}")
    public Boolean setEmployeeRole(@PathVariable Long employeeId, @RequestBody SetEmployeeRoleDTO setEmployeeRoleDTO) {
        roleManager.setRoleToEmployee(employeeId, setEmployeeRoleDTO.getRoleIds());
        return Boolean.TRUE;
    }

    @ApiOperation("新增员工信息（后管用户新增）")
    @PostMapping
    public Long save(@Validated @RequestBody EmployeeSaveDTO employeeDTO) {
        return employeeManager.saveEmployeeAndSetDepartment(employeeDTO);
    }

    @ApiOperation("修改员工信息")
    @PutMapping
    public Boolean update(@Validated @RequestBody EmployeeUpdateDTO employeeDTO) {
        return this.employeeService.update(employeeDTO);
    }

    @ApiOperation("员工启用，禁用")
    @PutMapping("enable")
    public Boolean enable(@Validated @NotNull Long id, @Validated @NotNull Boolean isEnable) {
        return this.employeeService.update(EmployeeUpdateDTO.builder().id(id).isEnable(isEnable).build());
    }

    @ApiOperation("删除员工信息")
    @DeleteMapping
    public Boolean delete(@Validated @NotNull @RequestParam("idList") List<Long> idList) {
        return this.employeeService.delete(idList);
    }

    @AuthAdmin
    @ApiOperation("获取当前登录的员工信息")
    @GetMapping("current")
    public EmployeeDO getCurrentEmployee() {
        return this.employeeService.getEmployee(UserContext.get().getEmployeeId());
    }

    @GetMapping("{id}")
    @ApiOperation("根据员工id 获取员工信息")
    public EmployeeDO getEmployee(@PathVariable Long id) {
        return this.employeeService.getEmployee(id);
    }


    @ApiOperation("获取部门下的员工")
    @GetMapping("department")
    public List<EmployeeVO> getDepartmentEmployee(Long departmentId) {
        return employeeService.listEmployee(departmentId);
    }

    @PutMapping("employee")
    @ApiOperation("设置员工的部门")
    public Boolean employeeToDepartmentRole(EmployeeToDepartmentRoleDTO employeeToDepartmentRoleDTO) {
        departmentManager.employeeToDepartmentRole(employeeToDepartmentRoleDTO);
        return Boolean.TRUE;
    }

    @PutMapping("password")
    @ApiOperation("重置密码")
    public Boolean resetPassword(Long employeeId) {
        employeeService.resetPassword(employeeId);
        return Boolean.TRUE;
    }
}

