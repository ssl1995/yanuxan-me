package com.msb.user.core.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.user.core.model.dto.SystemClientDTO;
import com.msb.user.core.model.entity.SystemClient;
import com.msb.user.core.model.vo.SystemClientVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 系统客户端表(SystemClient)表服务接口
 *
 * @author makejava
 * @date 2022-04-25 16:56:40
 */
@Mapper(componentModel = "spring")
public interface SystemClientConvert {

    SystemClientVO toVo(SystemClient systemClient);

    List<SystemClientVO> toVo(List<SystemClient> systemClient);

    Page<SystemClientVO> toVo(Page<SystemClient> systemClient);

    SystemClient toDo(SystemClientDTO systemClientDTO);
}

