package com.msb.mall.trade.service.convert;


import com.msb.mall.trade.model.entity.RefundOrderLog;
import com.msb.mall.trade.model.vo.admin.RefundLogInfoVO;
import com.msb.mall.trade.model.vo.admin.RefundLogVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 退款单操作记录(RefundOrderLog)表服务接口
 *
 * @author makejava
 * @date 2022-04-08 18:24:33
 */
@Mapper(componentModel = "spring")
public interface RefundOrderLogConvert {

    List<RefundLogVO> toRefundLogVOList(List<RefundOrderLog> refundOrderLogs);

    RefundLogVO toRefundLogVO(RefundOrderLog refundOrderLog);

    RefundLogInfoVO toRefundLogInfoVO(RefundOrderLog refundOrderLog);

}

