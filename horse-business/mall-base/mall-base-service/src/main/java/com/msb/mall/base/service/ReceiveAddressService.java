package com.msb.mall.base.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.common.context.UserContext;
import com.msb.mall.base.api.model.ReceiveAddressDO;
import com.msb.mall.base.model.vo.ReceiveAddressVO;
import com.msb.mall.base.mapper.ReceiveAddressMapper;
import com.msb.mall.base.model.entity.ReceiveAddress;
import com.msb.mall.base.model.dto.ReceiveAddressDTO;
import com.msb.mall.base.service.convert.ReceiveAddressConvert;
import org.springframework.stereotype.Service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.msb.framework.common.model.PageDTO;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Consumer;

/**
 * (ReceiveAddress)表服务实现类
 *
 * @author makejava
 * @date 2022-03-31 13:57:15
 */
@Service("receiveAddressService")
public class ReceiveAddressService extends ServiceImpl<ReceiveAddressMapper, ReceiveAddress> {

    @Resource
    private ReceiveAddressConvert receiveAddressConvert;

    private void setDefaultReceiveAddress() {
        this.lambdaUpdate().set(ReceiveAddress::getIsDefault, false).eq(ReceiveAddress::getUserId, UserContext.getUserId()).update();
    }

    public ReceiveAddressDO getDefaultReceiveAddress(Long userId) {
        ReceiveAddress receiveAddress = this.lambdaQuery().eq(ReceiveAddress::getUserId, userId).eq(ReceiveAddress::getIsDefault, Boolean.TRUE).one();
        return receiveAddressConvert.toDO(receiveAddress);
    }

    public IPage<ReceiveAddressVO> page(PageDTO pageDTO, ReceiveAddressDTO receiveAddressDTO) {
        return receiveAddressConvert.toVo(this.page(pageDTO.page(), new QueryWrapper<ReceiveAddress>().setEntity(receiveAddressConvert.toEntity(receiveAddressDTO))));
    }

    public List<ReceiveAddressVO> listByCurrentUserReceiveAddress() {
        Long userId = UserContext.getUserId();
        List<ReceiveAddress> list = this.lambdaQuery().eq(ReceiveAddress::getUserId, userId)
                .orderByDesc(ReceiveAddress::getIsDefault)
                .orderByDesc(ReceiveAddress::getCreateTime).list();
        List<ReceiveAddressVO> receiveAddressListVO = receiveAddressConvert.toVo(list);
        receiveAddressListVO.forEach(receiveAddressVO ->
                receiveAddressVO.setPhone(receiveAddressVO.getPhone().replaceAll("(\\d{3})\\d*(\\d{4})", "$1****$2")));
        return receiveAddressListVO;
    }

    @Transactional(rollbackFor = Exception.class)
    public ReceiveAddressVO save(ReceiveAddressDTO receiveAddressDTO) {
        receiveAddressDTO.setUserId(UserContext.getUserId());
        if (receiveAddressDTO.getIsDefault()) {
            setDefaultReceiveAddress();
        }
        ReceiveAddress receiveAddress = receiveAddressConvert.toEntity(receiveAddressDTO);
        this.save(receiveAddress);
        return receiveAddressConvert.toVo(receiveAddress);
    }

    @Transactional(rollbackFor = Exception.class)
    public ReceiveAddressVO update(ReceiveAddressDTO receiveAddressDTO) {
        if (receiveAddressDTO.getIsDefault()) {
            setDefaultReceiveAddress();
        }
        if (receiveAddressDTO.getPhone().contains("*")) {
            receiveAddressDTO.setPhone(null);
        }
        this.updateById(receiveAddressConvert.toEntity(receiveAddressDTO));
        return receiveAddressConvert.toVo(this.getById(receiveAddressDTO.getId()));
    }

    public Boolean delete(List<Long> idList) {
        return this.removeByIds(idList);
    }
}

