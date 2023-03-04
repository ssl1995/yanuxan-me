package com.msb.search.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.search.model.dto.SystemConfigDTO;
import com.msb.search.model.entity.SystemConfig;
import com.msb.search.model.vo.SystemConfigVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * (SystemConfig)表服务接口
 *
 * @author luozhan
 * @since 2022-06-10 15:34:47
 */
@Mapper(componentModel = "spring")
public interface SystemConfigConvert {

    SystemConfigVO toVo(SystemConfig systemConfig);

    List<SystemConfigVO> toVo(List<SystemConfig> systemConfig);

    Page<SystemConfigVO> toVo(Page<SystemConfig> systemConfig);

    SystemConfig toDo(SystemConfigDTO systemConfigDTO);
}

