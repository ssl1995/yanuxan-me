package com.msb.mall.product.controller.app;


import com.msb.framework.common.context.UserContext;
import com.msb.mall.product.model.dto.app.AddShoppingCartDTO;
import com.msb.mall.product.model.dto.app.SetShoppingCartDTO;
import com.msb.mall.product.model.vo.app.AddShoppingCartVO;
import com.msb.mall.product.model.vo.app.ShoppingCartVO;
import com.msb.mall.product.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * (ShoppingCart)表控制层
 *
 * @author makejava
 * @date 2022-03-31 16:16:10
 */
@Api(tags = "购物车相关接口")
@RestController
@RequestMapping("shoppingCart")
public class ShoppingCartController {
    /**
     * 服务对象
     */
    @Resource
    private ShoppingCartService shoppingCartService;

    @ApiOperation("购物车查询")
    @GetMapping
    public List<ShoppingCartVO> getCurrentUserShoppingCart() {
        return this.shoppingCartService.getCurrentUserShoppingCart(UserContext.getUserId());
    }

    @ApiOperation("购物车数量设置值")
    @PutMapping("number")
    public AddShoppingCartVO setShoppingCart(@RequestBody SetShoppingCartDTO shoppingCartDTO) {
        return this.shoppingCartService.setShoppingCart(UserContext.getUserId(), shoppingCartDTO.getId(), shoppingCartDTO.getNumber());
    }

    @ApiOperation("购物车库存加减")
    @PutMapping("increase")
    public AddShoppingCartVO increaseShoppingCart(@Validated @RequestBody AddShoppingCartDTO shoppingCartDTO) {
        return shoppingCartService.increaseShoppingCart(UserContext.getUserId(), shoppingCartDTO);
    }

    @ApiOperation("购物车删除")
    @DeleteMapping
    public Boolean delete(@Validated @NotNull Long[] idList) {
        return shoppingCartService.delete(UserContext.getUserId(), Arrays.asList(idList));
    }
}

