package com.msb.user.core.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.user.api.vo.MenuDO;
import com.msb.user.core.model.dto.MenuDTO;
import com.msb.user.core.model.entity.Menu;
import com.msb.user.core.model.vo.MenuTreeVO;
import com.msb.user.core.model.vo.MenuVO;
import com.msb.user.core.model.vo.RoleMenuTreeVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * (Menu)表服务接口
 *
 * @author makejava
 * @since 2022-03-23 20:13:01
 */
@Mapper(componentModel = "spring")
public interface MenuConvert {

    MenuVO toVo(Menu menu);

    MenuDO toDo(Menu menu);

    List<MenuTreeVO> toTreeVo(List<Menu> menuList);

    List<RoleMenuTreeVO> toRoleTreeVo(List<Menu> menuList);

    List<MenuDO> toDo(List<Menu> menu);

    Page<MenuDO> toDo(Page<Menu> menu);

    List<MenuVO> toVo(List<Menu> menu);

    Page<MenuVO> toVo(Page<Menu> menu);

    Menu toDo(MenuDTO menuDTO);
}

