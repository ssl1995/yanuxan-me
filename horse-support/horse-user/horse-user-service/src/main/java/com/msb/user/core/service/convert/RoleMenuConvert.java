package com.msb.user.core.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.user.core.model.entity.RoleMenu;
import com.msb.user.core.model.vo.RoleMenuVO;
import com.msb.user.core.model.dto.RoleMenuDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 系统角色菜单表(RoleMenu)表服务接口
 *
 * @author makejava
 * @date 2022-05-09 10:18:12
 */
@Mapper(componentModel = "spring")
public interface RoleMenuConvert {

    RoleMenuVO toVo(RoleMenu roleMenu);

    List<RoleMenuVO> toVo(List<RoleMenu> roleMenu);

    Page<RoleMenuVO> toVo(Page<RoleMenu> roleMenu);

    RoleMenu toDo(RoleMenuDTO roleMenuDTO);
}

