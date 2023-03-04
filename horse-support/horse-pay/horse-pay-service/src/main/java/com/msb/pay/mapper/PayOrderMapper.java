package com.msb.pay.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.msb.pay.model.entity.PayOrder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

/**
 * 支付订单表(PayOrder)表数据库访问层
 *
 * @author makejava
 * @date 2022-06-06 10:42:44
 */
public interface PayOrderMapper extends BaseMapper<PayOrder> {

    @Update("update pay_order set refund_amount = refund_amount + #{cumulativeAmount}, refund_times = refund_times + 1 where id = #{payOrderId} and amount >= refund_amount + #{cumulativeAmount}")
    boolean cumulativeRefundAmount(@Param("payOrderId") Long payOrderId, @Param("cumulativeAmount") BigDecimal cumulativeAmount);

}

