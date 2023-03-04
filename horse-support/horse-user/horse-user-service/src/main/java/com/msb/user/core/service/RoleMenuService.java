package com.msb.user.core.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.user.core.mapper.RoleMenuMapper;
import com.msb.user.core.model.entity.Menu;
import com.msb.user.core.model.entity.RoleMenu;
import com.msb.user.core.model.vo.RoleMenuVO;
import com.msb.user.core.model.dto.RoleMenuDTO;
import com.msb.user.core.service.convert.RoleMenuConvert;
import org.springframework.stereotype.Service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.msb.framework.common.model.PageDTO;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 系统角色菜单表(RoleMenu)表服务实现类
 *
 * @author makejava
 * @date 2022-05-09 10:18:12
 */
@Service("roleMenuService")
public class RoleMenuService extends ServiceImpl<RoleMenuMapper, RoleMenu> {

    @Resource
    private RoleMenuConvert roleMenuConvert;

    public List<RoleMenu> listRoleMenu(Long roleId) {
        return this.lambdaQuery().eq(RoleMenu::getRoleId, roleId).list();
    }

    public Set<Long> listRoleMenuId(List<Long> roleIdList) {
        if (roleIdList.isEmpty()) {
            return Collections.emptySet();
        }
        return this.lambdaQuery()
                .in(RoleMenu::getRoleId, roleIdList)
                .select(RoleMenu::getMenuId)
                .list()
                .stream()
                .map(RoleMenu::getMenuId)
                .collect(Collectors.toSet());
    }

    public void delete(Long roleId, List<Long> menuIdList) {
        if (menuIdList.isEmpty()) {
            return;
        }
        this.lambdaUpdate().eq(RoleMenu::getRoleId, roleId).in(RoleMenu::getMenuId, menuIdList).remove();
    }

    public IPage<RoleMenuVO> page(PageDTO pageDTO, RoleMenuDTO roleMenuDTO) {
        return roleMenuConvert.toVo(this.page(pageDTO.page(), new QueryWrapper<RoleMenu>().setEntity(roleMenuConvert.toDo(roleMenuDTO))));
    }

    public void delete(Long roleId) {
        this.lambdaUpdate().eq(RoleMenu::getRoleId, roleId).remove();
    }

    public RoleMenuVO getOne(Serializable id) {
        return roleMenuConvert.toVo(this.getById(id));
    }

    public Boolean save(RoleMenuDTO roleMenuDTO) {
        return this.save(roleMenuConvert.toDo(roleMenuDTO));
    }

    public Boolean update(RoleMenuDTO roleMenuDTO) {
        return this.updateById(roleMenuConvert.toDo(roleMenuDTO));
    }

    public Boolean delete(List<Long> idList) {
        return this.removeByIds(idList);
    }
}

