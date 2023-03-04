package com.msb.pay.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.pay.model.dto.RefundOrderPageDTO;
import com.msb.pay.model.entity.RefundOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 退款订单表(RefundOrder)表数据库访问层
 *
 * @author makejava
 * @date 2022-06-06 10:42:46
 */
public interface RefundOrderMapper extends BaseMapper<RefundOrder> {

    List<RefundOrder> refundOrderPage(@Param("dto") RefundOrderPageDTO refundOrderPageDTO, Page page);

}

