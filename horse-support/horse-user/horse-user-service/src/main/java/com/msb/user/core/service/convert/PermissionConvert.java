package com.msb.user.core.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.user.core.model.entity.Permission;
import com.msb.user.core.model.vo.PermissionVO;
import com.msb.user.core.model.dto.PermissionDTO;
import com.msb.user.core.model.vo.RolePermissionRelationVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 权限表(Permission)表服务接口
 *
 * @author makejava
 * @date 2022-05-09 10:18:11
 */
@Mapper(componentModel = "spring")
public interface PermissionConvert {

    PermissionVO toVo(Permission permission);

    List<PermissionVO> toVo(List<Permission> permission);

    List<RolePermissionRelationVO> toRolePermissionRelationVO(List<Permission> permission);

    Page<PermissionVO> toVo(Page<Permission> permission);

    Permission toEntity(PermissionDTO permissionDTO);

    List<Permission> toEntity(List<PermissionDTO> permissionDTO);
}

