package com.msb.mall.base.service.convert;

import com.msb.mall.base.model.vo.*;
import com.msb.mall.product.api.model.ProductOverviewDO;
import com.msb.mall.trade.api.model.*;
import com.msb.user.api.vo.UserOverviewDO;
import com.msb.user.api.vo.VisitorStatisticsByDayDO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 首页数据转换接口
 * @author 舒梦姣
 * @date 2022-05-31
 */
@Mapper(componentModel = "spring")
public interface FrontPageConvert {

    FrontPageProductVO toVo(ProductOverviewDO productOverviewDO);

    FrontPageWaitHandleVO toVo(TradeOrderWaitHandleDO waitHandleOrderCount);

    FrontPageUserVO toVo(UserOverviewDO userOverview);

    List<FrontPageSalesStatisticsByHourVO> toSalesHourVo(List<SalesStatisticsByHourDO> listTodaySalesStatisticsByHour);

    List<FrontPageSalesStatisticsByDayVO> toSalesDayVo(List<SalesStatisticsByDayDO> listSalesStatisticsByDay);

    List<FrontPageOrderStatisticsByHourVO> toOrderHourVo(List<OrderStatisticsByHourDO> listOrderStatisticsByHour);

    List<FrontPageOrderStatisticsByDayVO> toOrderDayVO(List<OrderStatisticsByDayDO> list);

    List<FrontPageVisitorStatisticsByDayVO> toVisitorDayVO(List<VisitorStatisticsByDayDO> list);
}
