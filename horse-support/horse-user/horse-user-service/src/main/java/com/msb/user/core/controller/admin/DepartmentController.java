package com.msb.user.core.controller.admin;


import com.msb.user.auth.AuthAdmin;
import com.msb.user.core.model.dto.DelRoleOfDepartmentDTO;
import com.msb.user.core.model.dto.DepartmentDTO;
import com.msb.user.core.model.dto.SetRoleToDepartmentDTO;
import com.msb.user.core.model.vo.DepartmentTreeVO;
import com.msb.user.core.model.vo.DepartmentVO;
import com.msb.user.core.service.DepartmentRoleService;
import com.msb.user.core.service.DepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * 部门表(Department)表控制层
 *
 * @author makejava
 * @date 2022-05-09 10:18:09
 */
@Api(tags = "（后管）部门相关接口")
@AuthAdmin
@RestController
@RequestMapping("department")
public class DepartmentController {
    /**
     * 服务对象
     */
    @Resource
    private DepartmentService departmentService;

    @Resource
    private DepartmentRoleService departmentRoleService;

    @ApiOperation("获取部门下的子部门")
    @GetMapping("child")
    public List<DepartmentVO> getDepartmentChild(Long id) {
        return departmentService.getDepartmentChild(id);
    }

    @ApiOperation("获取部门树形结构")
    @GetMapping("tree")
    public List<DepartmentTreeVO> getDepartmentTree(Boolean hiddenDisable) {
        return departmentService.getDepartmentTreeVO(hiddenDisable);
    }

    @ApiOperation("根据id 获取部门信息")
    @GetMapping("{id}")
    public DepartmentVO getOne(@PathVariable Serializable id) {
        return this.departmentService.getOne(id);
    }

    @ApiOperation("保存部门信息")
    @PostMapping
    public Boolean save(@RequestBody DepartmentDTO departmentDTO) {
        return this.departmentService.save(departmentDTO);
    }

    @ApiOperation("修改部门信息")
    @PutMapping("{id}")
    public Boolean update(@PathVariable Long id, @RequestBody DepartmentDTO departmentDTO) {
        return this.departmentService.update(id, departmentDTO);
    }

    @ApiOperation("删除部门信息")
    @DeleteMapping
    public Boolean delete(@RequestParam("idList") List<Long> idList) {
        return this.departmentService.delete(idList);
    }


    @ApiOperation("设置角色到部门")
    @PutMapping("role")
    public Boolean setRoleToDepartment(@RequestBody SetRoleToDepartmentDTO setRoleToDepartmentDTO) {
        return departmentRoleService.setRoleToDepartment(setRoleToDepartmentDTO);
    }

    @ApiOperation("删除角色到部门")
    @DeleteMapping("role")
    public Boolean delRoleOfDepartment(@RequestBody DelRoleOfDepartmentDTO delRoleOfDepartmentDTO) {
        return departmentRoleService.delRoleOfDepartment(delRoleOfDepartmentDTO);
    }
}

