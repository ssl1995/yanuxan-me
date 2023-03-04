package com.msb.mall.trade.service.convert;


import com.msb.mall.trade.model.entity.TradeOrderLogistics;
import com.msb.mall.trade.model.vo.app.LogisticsInfoAppVO;
import com.msb.mall.trade.model.vo.app.OrderLogisticsVO;
import org.mapstruct.Mapper;

/**
 * 订单物流信息(TradeOrderLogistics)表服务接口
 *
 * @author makejava
 * @since 2022-03-26 17:07:43
 */
@Mapper(componentModel = "spring")
public interface TradeOrderLogisticsConvert {

    OrderLogisticsVO toOrderLogisticsVO(TradeOrderLogistics tradeOrderLogistics);

    LogisticsInfoAppVO toLogisticsAppInfoVO(TradeOrderLogistics tradeOrderLogistics);

}

