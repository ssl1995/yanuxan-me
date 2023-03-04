package com.msb.pay.service.convert;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.pay.model.entity.PayOrder;
import com.msb.pay.model.vo.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * 支付订单表(PayOrder)表服务接口
 *
 * @author makejava
 * @date 2022-06-06 10:42:45
 */
@Mapper(componentModel = "spring")
public interface PayOrderConvert {

    @Mapping(target = "payOrderId", source = "id")
    PayOrderVO toPayOrderVO(PayOrder payOrder);

    @Mapping(target = "payOrderId", source = "id")
    PayOrderPageVO toPayOrderPageVO(PayOrder payOrder);

    Page<PayOrderPageVO> toPayOrderVOPage(Page<PayOrder> page);

    @Mapping(target = "payOrderId", source = "id")
    PayOrderSimpleVO toPayOrderSimpleVO(PayOrder payOrder);

    @Mapping(target = "payOrderId", source = "id")
    PrepayOrderVO toPrepayOrderVO(PayOrder payOrder);

    @Mapping(target = "payOrderId", source = "id")
    PayResultVO toPayResultVO(PayOrder payOrder);

}

