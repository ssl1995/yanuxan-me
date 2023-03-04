package com.msb.mall.trade.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.mall.product.api.model.ProductSkuOccupyDTO;
import com.msb.mall.trade.api.model.TradeOrderProductDO;
import com.msb.mall.trade.model.entity.TradeOrderProduct;
import com.msb.mall.trade.model.vo.admin.OrderProductAdminVO;
import com.msb.mall.trade.model.vo.app.AdvanceOrderProductVO;
import com.msb.mall.trade.model.vo.app.OrderProductAfterSaleVO;
import com.msb.mall.trade.model.vo.app.OrderProductVO;
import com.msb.mall.trade.model.vo.app.RefundProductVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * 订单详情(TradeOrderProduct)表服务接口
 *
 * @author makejava
 * @since 2022-03-24 18:30:17
 */
@Mapper(componentModel = "spring")
public interface TradeOrderProductConvert {

    @Mapping(target = "orderProductId", source = "id")
    OrderProductVO toOrderProductVO(TradeOrderProduct orderProduct);

    List<OrderProductVO> toOrderProductVOList(List<TradeOrderProduct> orderProductList);

    @Mapping(target = "orderProductId", source = "id")
    OrderProductAfterSaleVO toOrderProductAfterSaleVO(TradeOrderProduct orderProduct);

    List<OrderProductAfterSaleVO> toOrderProductAfterSaleVOList(List<TradeOrderProduct> orderProductList);

    List<AdvanceOrderProductVO> toAdvanceOrderProductVOList(List<TradeOrderProduct> orderProductList);

    AdvanceOrderProductVO toAdvanceOrderProductVO(TradeOrderProduct orderProduct);

    @Mapping(target = "orderProductId", source = "id")
    OrderProductAdminVO toOrderProductAdminVO(TradeOrderProduct orderProduct);

    List<OrderProductAdminVO> toOrderProductAdminVOList(List<TradeOrderProduct> orderProductList);

    List<ProductSkuOccupyDTO> toProductSkuOccupyDTOList(List<TradeOrderProduct> orderProductList);

    @Mapping(target = "skuId", source = "productSkuId")
    @Mapping(target = "skuName", source = "skuDescribe")
    ProductSkuOccupyDTO toProductSkuOccupyDTO(TradeOrderProduct orderProduct);

    @Mapping(target = "orderProductId", source = "id")
    RefundProductVO toRefundProductVO(TradeOrderProduct orderProduct);

    List<TradeOrderProductDO> toTradeOrderProductDO(List<TradeOrderProduct> list);

    TradeOrderProductDO toTradeOrderProductDO(TradeOrderProduct tradeOrderProduct);

    Page<OrderProductVO> toOrderProductVOPage(Page<TradeOrderProduct> page);
}

