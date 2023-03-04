package com.msb.user.core.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.user.core.model.dto.SystemDTO;
import com.msb.user.core.model.entity.System;
import com.msb.user.core.model.vo.SystemVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 系统表(System)表服务接口
 *
 * @author makejava
 * @date 2022-04-25 16:56:41
 */
@Mapper(componentModel = "spring")
public interface SystemConvert {

    SystemVO toVo(System system);

    List<SystemVO> toVo(List<System> system);

    Page<SystemVO> toVo(Page<System> system);

    System toDo(SystemDTO systemDTO);
}

