package com.msb.user.core.controller.admin;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.msb.user.auth.AuthAdmin;
import com.msb.user.core.model.dto.PermissionDTO;
import com.msb.user.core.model.vo.PermissionVO;
import com.msb.user.core.service.PermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.swing.border.EtchedBorder;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 权限表(Permission)表控制层
 *
 * @author makejava
 * @date 2022-05-09 10:18:11
 */
@Api(tags = "（后管）权限相关接口")
@RestController
@AuthAdmin
@RequestMapping("permission")
public class PermissionController {
    /**
     * 服务对象
     */
    @Resource
    private PermissionService permissionService;

    @ApiOperation("根据菜单获取权限列表")
    @GetMapping
    public List<PermissionVO> listPermission(@Validated @NotNull Long menuId) {
        return permissionService.listPermission(menuId);
    }

    @ApiOperation("保存权限")
    @PostMapping
    public Boolean save(@Validated @RequestBody PermissionDTO permissionDTO) {
        return this.permissionService.save(permissionDTO);
    }

    @ApiOperation("修改")
    @PutMapping("{id}")
    public Boolean update(@PathVariable Long id, @Validated @RequestBody PermissionDTO permissionDTO) {
        return this.permissionService.update(id, permissionDTO);
    }

    @ApiOperation("删除")
    @DeleteMapping
    public Boolean delete(@RequestParam("idList") List<Long> idList) {
        return this.permissionService.delete(idList);
    }

    @ApiOperation("通过swagger文档，更新接口权限")
    @PostMapping("swagger")
    public Boolean swaggerDoc(String service, Long systemId, String filterKeyword, String json) {
        if (StringUtils.isBlank(filterKeyword)) {
            filterKeyword = "后管";
        }
        if (StringUtils.isBlank(service)) {
            service = JSON.parseObject(json).getJSONObject("info").getString("title");
        }
        permissionService.saveSwaggerApiDoc(service, systemId, filterKeyword, json);
        return Boolean.TRUE;
    }

    @ApiOperation("查询未分配的接口")
    @GetMapping("NotDistribution")
    public List<PermissionVO> listNotDistributionPermission() {
        return permissionService.listNotDistributionPermission();
    }

    @ApiOperation("分配权限所属菜单")
    @PutMapping("menu")
    public Boolean distributionPermissionMenu(Long permissionId, Long menuId) {
        return permissionService.distributionPermissionMenu(permissionId, menuId);
    }
}

