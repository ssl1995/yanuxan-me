package com.msb.user.core.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.user.core.mapper.RolePermissionMapper;
import com.msb.user.core.model.entity.Permission;
import com.msb.user.core.model.entity.RolePermission;
import com.msb.user.core.model.vo.RolePermissionRelationVO;
import com.msb.user.core.model.vo.RolePermissionVO;
import com.msb.user.core.model.dto.RolePermissionDTO;
import com.msb.user.core.service.convert.RolePermissionConvert;
import org.springframework.stereotype.Service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.msb.framework.common.model.PageDTO;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统角色权限表(RolePermission)表服务实现类
 *
 * @author makejava
 * @date 2022-05-09 10:18:12
 */
@Service("rolePermissionService")
public class RolePermissionService extends ServiceImpl<RolePermissionMapper, RolePermission> {

    @Resource
    private RolePermissionConvert rolePermissionConvert;

    public void delete(Long roleId, List<Long> permissionIdList) {
        if (permissionIdList.isEmpty()) {
            return;
        }
        this.lambdaUpdate().eq(RolePermission::getRoleId, roleId).in(RolePermission::getPermissionId, permissionIdList).remove();
    }

    public void delete(Long roleId) {
        this.lambdaUpdate().eq(RolePermission::getRoleId, roleId).remove();
    }

    public List<RolePermission> listRolePermission(Long roleId) {
        return this.lambdaQuery().eq(RolePermission::getRoleId, roleId).list();
    }

    public List<RolePermission> listRolePermission(List<Long> roleIds) {
        if (roleIds.isEmpty()) {
            return Collections.emptyList();
        }
        return this.lambdaQuery().in(RolePermission::getRoleId, roleIds).list();
    }

    public List<Long> getRoleIdListPermission(Long permissionId) {
        List<RolePermission> list = this.lambdaQuery().eq(RolePermission::getPermissionId, permissionId).list();
        return list.stream().map(RolePermission::getRoleId).collect(Collectors.toList());
    }

    public IPage<RolePermissionVO> page(PageDTO pageDTO, RolePermissionDTO rolePermissionDTO) {
        return rolePermissionConvert.toVo(this.page(pageDTO.page(), new QueryWrapper<RolePermission>().setEntity(rolePermissionConvert.toDo(rolePermissionDTO))));
    }

    public RolePermissionVO getOne(Serializable id) {
        return rolePermissionConvert.toVo(this.getById(id));
    }

    public Boolean save(RolePermissionDTO rolePermissionDTO) {
        return this.save(rolePermissionConvert.toDo(rolePermissionDTO));
    }

    public Boolean update(RolePermissionDTO rolePermissionDTO) {
        return this.updateById(rolePermissionConvert.toDo(rolePermissionDTO));
    }

    public Boolean delete(List<Long> idList) {
        return this.removeByIds(idList);
    }
}

