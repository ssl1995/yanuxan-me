package com.msb.pay.service.convert;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.pay.model.entity.RefundOrder;
import com.msb.pay.model.vo.RefundOrderPageVO;
import com.msb.pay.model.vo.RefundOrderVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * 退款订单表(RefundOrder)表服务接口
 *
 * @author makejava
 * @date 2022-06-06 10:42:46
 */
@Mapper(componentModel = "spring")
public interface RefundOrderConvert {

    @Mapping(target = "refundOrderId", source = "id")
    RefundOrderVO toRefundOrderVO(RefundOrder refundOrder);

    @Mapping(target = "refundOrderId", source = "id")
    RefundOrderPageVO toRefundOrderPageVO(RefundOrder refundOrder);

    Page<RefundOrderPageVO> toRefundOrderPage(Page<RefundOrder> page);

}

