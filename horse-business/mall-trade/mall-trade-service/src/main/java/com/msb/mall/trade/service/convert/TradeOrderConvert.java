package com.msb.mall.trade.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.mall.product.api.model.ShoppingCartDO;
import com.msb.mall.trade.api.model.OrderStatisticsByHourDO;
import com.msb.mall.trade.api.model.SalesStatisticsByHourDO;
import com.msb.mall.trade.model.dto.app.OrderSubmitProductDTO;
import com.msb.mall.trade.model.entity.SalesStatistics;
import com.msb.mall.trade.model.entity.TradeOrder;
import com.msb.mall.trade.model.vo.admin.OrderDeliveryVO;
import com.msb.mall.trade.model.vo.admin.OrderInfoAdminVO;
import com.msb.mall.trade.model.vo.admin.OrderListAdminVO;
import com.msb.mall.trade.model.vo.admin.RefundTradeOrderInfoVO;
import com.msb.mall.trade.model.vo.app.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * 交易订单(TradeOrder)表服务接口
 *
 * @author makejava
 * @since 2022-03-24 18:30:16
 */
@Mapper(componentModel = "spring")
public interface TradeOrderConvert {

    Page<OrderListAdminVO> toOrderListAdminVOPage(Page<TradeOrder> page);

    Page<OrderListAppVO> toOrderListAppVOPage(Page<TradeOrder> page);

    List<OrderSubmitProductDTO> toShoppingCartProductDTOList(List<ShoppingCartDO> shoppingCarts);

    @Mapping(target = "quantity", source = "number")
    OrderSubmitProductDTO toSubmitOrderProductDTO(ShoppingCartDO shoppingCart);

    @Mapping(target = "orderId", source = "id")
    OrderListAppVO toAppListVO(TradeOrder tradeOrder);

    @Mapping(target = "orderId", source = "id")
    OrderInfoAppVO toAppInfoVO(TradeOrder tradeOrder);

    AdvanceOrderVO toAdvanceOrderVO(TradeOrder tradeOrder);

    @Mapping(target = "orderId", source = "id")
    OrderListAdminVO toOrderAdminListVO(TradeOrder tradeOrder);

    @Mapping(target = "orderId", source = "id")
    OrderInfoAdminVO toOrderAdminInfoVO(TradeOrder tradeOrder);

    @Mapping(target = "orderId", source = "id")
    OrderDeliveryVO toOrderDeliveryVO(TradeOrder tradeOrder);

    @Mapping(target = "orderId", source = "id")
    RefundTradeOrderInfoVO toRefundTradeOrderInfoVO(TradeOrder tradeOrder);

    @Mapping(target = "orderId", source = "id")
    OrderProductInfoVO toOrderProductInfoVO(TradeOrder tradeOrder);

    @Mapping(target = "orderId", source = "id")
    OrderPayResultVO toOrderPayResultVO(TradeOrder tradeOrder);

    @Mapping(target = "orderId", source = "id")
    OrderSubmitVO toOrderSubmitVO(TradeOrder tradeOrder);

    Page<OrderLogisticsListVO> toOrderLogisticsListVOPage(Page<TradeOrder> page);

    @Mapping(target = "orderId", source = "id")
    OrderLogisticsListVO toOrderLogisticsListVO(TradeOrder tradeOrder);
}

