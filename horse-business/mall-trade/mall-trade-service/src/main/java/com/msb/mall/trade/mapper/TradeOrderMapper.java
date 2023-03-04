package com.msb.mall.trade.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.msb.mall.trade.api.model.OrderStatisticsByHourDO;
import com.msb.mall.trade.api.model.SalesStatisticsByHourDO;
import com.msb.mall.trade.model.entity.SalesStatistics;
import com.msb.mall.trade.model.entity.TradeOrder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 交易订单(TradeOrder)表数据库访问层
 *
 * @author makejava
 * @since 2022-03-24 18:30:16
 */
public interface TradeOrderMapper extends BaseMapper<TradeOrder> {

    @Update("update trade_order set refund_amount = refund_amount + #{cumulativeAmount} where id = #{orderId} and pay_amount >= refund_amount + #{cumulativeAmount}")
    boolean cumulativeRefundAmount(@Param("orderId") Long orderId, @Param("cumulativeAmount") BigDecimal cumulativeAmount);

    @Select("SELECT SUM(total_amount) FROM trade_order WHERE order_status in ('3','4','5','6','7') AND (create_time BETWEEN #{beginDateTime} AND #{endDateTime}) AND is_deleted = 0")
    BigDecimal getSales(@Param("beginDateTime") LocalDateTime beginDateTime, @Param("endDateTime") LocalDateTime endDateTime);


    /**
     * 查询一天中每小时的销售数据，如果没有则返回0
     * @param dayOfStatics
     * @return 每小时的销售数据
     */
    List<SalesStatisticsByHourDO> listSalesStaticsByHour(@Param("dayOfStatics") LocalDate dayOfStatics);

    /**
     * 查询一天中每小时的订单数据，如果没有则返回0
     * @param dayOfStatics
     * @return 每小时的订单数据
     */
    List<OrderStatisticsByHourDO> listOrderStaticsByHour(@Param("dayOfStatics") LocalDate dayOfStatics);

}

