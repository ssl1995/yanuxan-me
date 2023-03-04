package com.msb.user.core.controller.admin;


import com.msb.user.auth.AuthAdmin;
import com.msb.user.core.manager.RoleManager;
import com.msb.user.core.model.dto.MenuDTO;
import com.msb.user.core.model.vo.MenuTreeVO;
import com.msb.user.core.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Menu)表控制层
 *
 * @author makejava
 * @since 2022-03-23 20:13:01
 */
@Api(tags = "（B端后管访问）菜单相关接口")
@RestController
@RequestMapping("menu")
@AuthAdmin
public class MenuController {
    /**
     * 服务对象
     */
    @Resource
    private MenuService menuService;

    @Resource
    private RoleManager roleManager;

    @ApiOperation("获取拥有的菜单权限")
    @GetMapping
    public List<MenuTreeVO> listEmployeeMenuTree() {
        return roleManager.listEmployeeMenuTree();
    }

    @ApiOperation("获取菜单树形结构")
    @GetMapping("tree")
    public List<MenuTreeVO> listMenuTree(Integer systemId) {
        return menuService.menuTree(systemId);
    }

    @ApiOperation("增加菜单")
    @PostMapping
    public Boolean save(@RequestBody MenuDTO menuDTO) {
        return this.menuService.save(menuDTO);
    }

    @ApiOperation("修改菜单")
    @PutMapping("{id}")
    public Boolean update(@PathVariable Long id, @RequestBody MenuDTO menuDTO) {
        return this.menuService.update(id, menuDTO);
    }

    @ApiOperation("删除菜单")
    @DeleteMapping
    public Boolean delete(@RequestParam("idList") List<Long> idList) {
        return this.menuService.delete(idList);
    }
}

