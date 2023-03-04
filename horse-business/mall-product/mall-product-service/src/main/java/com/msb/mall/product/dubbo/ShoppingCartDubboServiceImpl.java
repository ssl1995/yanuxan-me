package com.msb.mall.product.dubbo;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.msb.framework.common.exception.BizException;
import com.msb.mall.product.api.dubbo.ShoppingCartDubboService;
import com.msb.mall.product.api.model.ShoppingCartDO;
import com.msb.mall.product.model.entity.ShoppingCart;
import com.msb.mall.product.service.ShoppingCartService;
import com.msb.mall.product.service.convert.ShoppingCartConvert;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@DubboService
public class ShoppingCartDubboServiceImpl implements ShoppingCartDubboService {


    @Resource
    private ShoppingCartService shoppingCartService;

    @Resource
    private ShoppingCartConvert shoppingCartConvert;

    @Override
    public List<ShoppingCartDO> listShoppingCartByIds(List<Long> shoppingCartIds) {
        if (Objects.isNull(shoppingCartIds) || shoppingCartIds.isEmpty()) {
            throw new BizException("");
        }
        List<ShoppingCart> list = shoppingCartService.list(new LambdaQueryWrapper<ShoppingCart>()
                .in(ShoppingCart::getId, shoppingCartIds));
        return shoppingCartConvert.toDo(list);
    }

    @Override
    public Boolean removeShoppingCartByIds(List<Long> shoppingCartIds) {
        return shoppingCartService.removeByIds(shoppingCartIds);
    }

}
