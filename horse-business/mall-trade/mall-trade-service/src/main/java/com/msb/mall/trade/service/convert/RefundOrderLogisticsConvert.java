package com.msb.mall.trade.service.convert;


import com.msb.mall.trade.model.entity.RefundOrderLogistics;
import com.msb.mall.trade.model.vo.app.RefundLogisticsVO;
import org.mapstruct.Mapper;

/**
 * 退货单物流信息(RefundOrderLogistics)表服务接口
 *
 * @author makejava
 * @date 2022-04-08 18:24:34
 */
@Mapper(componentModel = "spring")
public interface RefundOrderLogisticsConvert {

    RefundLogisticsVO toRefundLogisticsVO(RefundOrderLogistics refundOrderLogistics);

}

