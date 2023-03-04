package com.msb.business.service.convert;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.business.api.model.HorseUserVO;
import com.msb.business.model.dto.HorseUserDTO;
import com.msb.business.model.entity.HorseUser;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HorseUserConvert {

    HorseUserVO toVo(HorseUser horseUser);

    List<HorseUserVO> toVo(List<HorseUser> horseUser);

    Page<HorseUserVO> toVo(Page<HorseUser> horseUser);

    HorseUser toDo(HorseUserDTO horseUserDTO);
}
