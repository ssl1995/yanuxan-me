package com.msb.mall.product.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.mall.product.api.model.ShoppingCartDO;
import com.msb.mall.product.model.dto.app.AddShoppingCartDTO;
import com.msb.mall.product.model.entity.ShoppingCart;
import com.msb.mall.product.model.vo.app.ShoppingCartVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * (ShoppingCart)表服务接口
 *
 * @author makejava
 * @date 2022-03-31 16:16:10
 */
@Mapper(componentModel = "spring")
public interface ShoppingCartConvert {

    ShoppingCartVO toVo(ShoppingCart shoppingCart);

    List<ShoppingCartVO> toVo(List<ShoppingCart> shoppingCart);

    Page<ShoppingCartVO> toVo(Page<ShoppingCart> shoppingCart);

    ShoppingCart toEntity(AddShoppingCartDTO shoppingCartDTO);

    ShoppingCartDO toDo(ShoppingCart shoppingCart);

    List<ShoppingCartDO> toDo(List<ShoppingCart> shoppingCart);
}

