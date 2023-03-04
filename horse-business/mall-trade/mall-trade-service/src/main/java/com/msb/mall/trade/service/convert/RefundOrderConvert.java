package com.msb.mall.trade.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.mall.base.api.model.RefundAddressDO;
import com.msb.mall.trade.model.entity.RefundOrder;
import com.msb.mall.trade.model.vo.admin.*;
import com.msb.mall.trade.model.vo.app.RefundInfoAppVO;
import com.msb.mall.trade.model.vo.app.RefundListAppVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * 退款订单表(RefundOrder)表服务接口
 *
 * @author makejava
 * @date 2022-04-08 18:24:33
 */
@Mapper(componentModel = "spring")
public interface RefundOrderConvert {

    Page<RefundListAppVO> toRefundListAppVOPage(Page<RefundOrder> page);

    @Mapping(target = "refundId", source = "id")
    RefundListAppVO toRefundListAppVO(RefundOrder refundOrder);

    @Mapping(target = "refundId", source = "id")
    RefundInfoAppVO toRefundInfoAppVO(RefundOrder refundOrder);

    Page<RefundListAdminVO> toRefundListAdminVOPage(Page<RefundOrder> page);

    @Mapping(target = "refundId", source = "id")
    RefundListAdminVO toRefundListAdminVO(RefundOrder refundOrder);

    @Mapping(target = "refundId", source = "id")
    RefundInfoAdminVO toRefundInfoAdminVO(RefundOrder refundOrder);

    RefundInfoByOnlyRefundVO toRefundInfoByOnlyRefundVO(RefundInfoAdminVO refundInfoAdminVO);

    RefundInfoByReturnVO toRefundInfoByReturnVO(RefundInfoAdminVO refundInfoAdminVO);

    @Mapping(target = "recipientName", source = "receiveName")
    @Mapping(target = "recipientPhone", source = "receivePhone")
    RefundAddressVO toRefundAddressVO(RefundAddressDO refundAddressDO);
}

