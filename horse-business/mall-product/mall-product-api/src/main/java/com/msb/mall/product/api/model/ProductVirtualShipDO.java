package com.msb.mall.product.api.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 商品虚拟发货DO
 * @author shumengjiao
 * @date 2022-05-25
 */
@Data
@Builder
public class ProductVirtualShipDO implements Serializable {
    /**
     * skuId
     */
    private Long skuId;

    /**
     * 虚拟发货信息
     */
    private List<VirtualProductDO> virtualProductDOList;
}
