package com.msb.mall.trade.service.convert;


import com.msb.mall.trade.model.entity.TradeOrderLog;
import com.msb.mall.trade.model.vo.admin.OrderLogVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 订单操作记录(TradeOrderLog)表服务接口
 *
 * @author makejava
 * @since 2022-03-24 18:30:18
 */
@Mapper(componentModel = "spring")
public interface TradeOrderLogConvert {

    OrderLogVO toOrderLogVO(TradeOrderLog tradeOrderLog);

    List<OrderLogVO> toOrderLogVOList(List<TradeOrderLog> tradeOrderLogList);

}

