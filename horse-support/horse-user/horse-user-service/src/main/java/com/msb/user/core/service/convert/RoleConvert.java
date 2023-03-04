package com.msb.user.core.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.user.core.model.entity.Role;
import com.msb.user.core.model.vo.RolePageVO;
import com.msb.user.core.model.vo.RoleVO;
import com.msb.user.core.model.dto.RoleDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 系统角色表(Role)表服务接口
 *
 * @author makejava
 * @date 2022-05-09 10:18:12
 */
@Mapper(componentModel = "spring")
public interface RoleConvert {

    RoleVO toVo(Role role);

    List<RoleVO> toVo(List<Role> role);

    Page<RoleVO> toVo(Page<Role> role);

    Page<RolePageVO> toPageVo(Page<Role> role);

    Role toDo(RoleDTO roleDTO);
}

