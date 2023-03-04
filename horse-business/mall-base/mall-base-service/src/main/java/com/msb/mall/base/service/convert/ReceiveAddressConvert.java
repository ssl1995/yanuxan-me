package com.msb.mall.base.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.mall.base.api.model.ReceiveAddressDO;
import com.msb.mall.base.model.entity.ReceiveAddress;
import com.msb.mall.base.model.dto.ReceiveAddressDTO;
import com.msb.mall.base.model.vo.ReceiveAddressVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * (ReceiveAddress)表服务接口
 *
 * @author makejava
 * @date 2022-03-31 13:57:15
 */
@Mapper(componentModel = "spring")
public interface ReceiveAddressConvert {

    ReceiveAddressVO toVo(ReceiveAddress receiveAddress);

    List<ReceiveAddressVO> toVo(List<ReceiveAddress> receiveAddress);

    Page<ReceiveAddressVO> toVo(Page<ReceiveAddress> receiveAddress);

    ReceiveAddress toEntity(ReceiveAddressDTO receiveAddressDTO);

    ReceiveAddressDO toDO(ReceiveAddress receiveAddress);
}

