package com.msb.user.core.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.common.utils.TreeUtils;
import com.msb.user.api.vo.MenuDO;
import com.msb.user.core.mapper.MenuMapper;
import com.msb.user.core.model.dto.MenuDTO;
import com.msb.user.core.model.entity.Menu;
import com.msb.user.core.model.vo.MenuTreeVO;
import com.msb.user.core.model.vo.MenuVO;
import com.msb.user.core.model.vo.RoleMenuTreeVO;
import com.msb.user.core.service.convert.MenuConvert;
import org.springframework.stereotype.Service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.msb.framework.common.model.PageDTO;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.Serializable;
import java.util.*;

/**
 * (Menu)表服务实现类
 *
 * @author makejava
 * @since 2022-03-23 20:13:01
 */
@Service("menuService")
public class MenuService extends ServiceImpl<MenuMapper, Menu> {

    @Resource
    private MenuConvert menuConvert;

    public IPage<MenuVO> page(PageDTO pageDTO, MenuDTO menuDTO) {
        return menuConvert.toVo(this.page(pageDTO.page(), new QueryWrapper<Menu>().setEntity(menuConvert.toDo(menuDTO))));
    }

    public List<MenuTreeVO> menuTree(Integer systemId) {
        return buildMenuTree(listMenuTreeVO(systemId), MenuTreeVO.class);
    }

    public List<MenuTreeVO> listMenuTreeVO(Integer systemId) {
        List<Menu> list = this.lambdaQuery().eq(Menu::getSystemId, systemId).orderByAsc(Menu::getSort).list();
        return menuConvert.toTreeVo(list);
    }

    public List<RoleMenuTreeVO> listRoleMenuTreeVO(Long systemId) {
        List<Menu> list = this.lambdaQuery().eq(Menu::getSystemId, systemId).orderByAsc(Menu::getSort).list();
        return menuConvert.toRoleTreeVo(list);
    }

    public List<RoleMenuTreeVO> listSystemMenuRoleIds(Integer systemId, List<Long> menuIds) {
        List<Menu> list = this.lambdaQuery().eq(Menu::getSystemId, systemId).in(Menu::getId, menuIds).list();
        return menuConvert.toRoleTreeVo(list);
    }

    public <T> List<T> buildMenuTree(List<T> list, Class<T> tClass) {
        Collection<T> menuTree = TreeUtils.toTree(list, "id", "parentId", "menuChild", tClass);
        if (Objects.isNull(menuTree)) {
            return Collections.emptyList();
        }
        return new ArrayList<>(menuTree);
    }

    public MenuVO getOne(Serializable id) {
        return menuConvert.toVo(this.getById(id));
    }

    public Boolean save(MenuDTO menuDTO) {
        return this.save(menuConvert.toDo(menuDTO).setIsEnable(true));
    }

    public Boolean update(Long id, MenuDTO menuDTO) {
        return this.updateById(menuConvert.toDo(menuDTO).setId(id));
    }

    public Boolean delete(List<Long> idList) {
        return this.removeByIds(idList);
    }
}

