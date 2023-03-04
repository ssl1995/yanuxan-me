package com.msb.business.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.business.model.dto.HorseUserDTO;
import com.msb.business.model.entity.HorseUser;
import com.msb.business.model.vo.HorseUserVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * (HorseUser)表服务接口
 *
 * @author makejava
 * @since 2022-03-21 14:57:36
 */
@Mapper(componentModel = "spring")
public interface HorseUserConvert {

    HorseUserVO toVo(HorseUser horseUser);

    List<HorseUserVO> toVo(List<HorseUser> horseUser);

    Page<HorseUserVO> toVo(Page<HorseUser> horseUser);

    HorseUser toDo(HorseUserDTO horseUserDTO);
}

