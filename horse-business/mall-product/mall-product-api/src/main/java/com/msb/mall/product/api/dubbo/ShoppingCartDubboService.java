package com.msb.mall.product.api.dubbo;

import com.msb.mall.product.api.model.ShoppingCartDO;

import java.util.List;

public interface ShoppingCartDubboService {

    /**
     * 根据购物车id 批量查询购物车列表
     * @param shoppingCartIds
     * @return
     */
    List<ShoppingCartDO> listShoppingCartByIds(List<Long> shoppingCartIds);

    /**
     * 根据id批量删除购物车记录
     * @param shoppingCartIds
     * @return
     */
    Boolean removeShoppingCartByIds(List<Long> shoppingCartIds);
}
