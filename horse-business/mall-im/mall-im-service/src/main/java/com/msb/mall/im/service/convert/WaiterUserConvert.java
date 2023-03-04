package com.msb.mall.im.service.convert;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.im.api.dto.AddStoreWaiterDTO;
import com.msb.im.api.dto.StoreWaiterDTO;
import com.msb.im.api.dto.UpdateStoreWaiterDTO;
import com.msb.mall.im.model.dto.AddWaiterUserDTO;
import com.msb.mall.im.model.dto.UpdateWaiterUserDTO;
import com.msb.mall.im.model.entity.WaiterUser;
import com.msb.mall.im.model.vo.WaiterUserVO;
import org.mapstruct.Mapper;

/**
 * 客服用户关联
 *
 * @author zhou miao
 * @date 2022/06/11
 */
@Mapper(componentModel = "spring")
public interface WaiterUserConvert {

    WaiterUser toDo(AddWaiterUserDTO addWaiterUserDTO);

    Page<WaiterUserVO> toVo(Page<WaiterUser> page);

    WaiterUser toDo(UpdateWaiterUserDTO updateWaiterUserDTO);

    AddStoreWaiterDTO toAddDTO(WaiterUser waiterUser);

    UpdateStoreWaiterDTO toUpdateDTO(WaiterUser waiterUser);

    WaiterUserVO toVo(WaiterUser waiterUser);
}
