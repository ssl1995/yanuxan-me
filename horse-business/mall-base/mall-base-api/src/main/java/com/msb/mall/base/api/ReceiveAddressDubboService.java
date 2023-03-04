package com.msb.mall.base.api;

import com.msb.mall.base.api.model.ReceiveAddressDO;

public interface ReceiveAddressDubboService {

    /**
     * 根据主键id 获取收货地址详细信息
     * @param receiveAddressId
     * @return
     */
    ReceiveAddressDO getReceiveAddressById(Long receiveAddressId);

    ReceiveAddressDO getDefaultReceiveAddress(Long userId);
}
