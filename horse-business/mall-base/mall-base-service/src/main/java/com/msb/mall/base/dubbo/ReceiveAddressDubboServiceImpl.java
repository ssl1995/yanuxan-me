package com.msb.mall.base.dubbo;

import com.msb.mall.base.api.ReceiveAddressDubboService;
import com.msb.mall.base.api.model.ReceiveAddressDO;
import com.msb.mall.base.model.entity.ReceiveAddress;
import com.msb.mall.base.service.ReceiveAddressService;
import com.msb.mall.base.service.convert.ReceiveAddressConvert;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Objects;

@DubboService
public class ReceiveAddressDubboServiceImpl implements ReceiveAddressDubboService {

    @Resource
    private ReceiveAddressService receiveAddressService;

    @Resource
    private ReceiveAddressConvert receiveAddressConvert;

    private static final String[] REMOTE_AREA_CODE = {
            "650000", "540000", "630000"
    };

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ReceiveAddressDO getReceiveAddressById(Long receiveAddressId) {
        ReceiveAddress receiveAddress = receiveAddressService.getById(receiveAddressId);
        ReceiveAddressDO receiveAddressDO = receiveAddressConvert.toDO(receiveAddress);
        if (Objects.nonNull(receiveAddressDO)) {
            receiveAddressDO.setIsRemoteArea(Arrays.asList(REMOTE_AREA_CODE).contains(receiveAddress.getProvinceCode()));
        }
        return receiveAddressDO;
    }

    @Override
    public ReceiveAddressDO getDefaultReceiveAddress(Long userId) {
        ReceiveAddressDO defaultReceiveAddress = receiveAddressService.getDefaultReceiveAddress(userId);
        if (Objects.nonNull(defaultReceiveAddress)) {
            defaultReceiveAddress.setIsRemoteArea(Arrays.asList(REMOTE_AREA_CODE).contains(defaultReceiveAddress.getProvinceCode()));
        }
        return defaultReceiveAddress;
    }
}
