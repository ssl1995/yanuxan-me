package com.msb.mall.product.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.common.context.UserContext;
import com.msb.framework.common.exception.BizException;
import com.msb.framework.web.result.BizAssert;
import com.msb.mall.product.mapper.ShoppingCartMapper;
import com.msb.mall.product.model.dto.app.AddShoppingCartDTO;
import com.msb.mall.product.model.entity.ShoppingCart;
import com.msb.mall.product.model.vo.app.AddShoppingCartVO;
import com.msb.mall.product.model.vo.app.ProductSimpleVO;
import com.msb.mall.product.model.vo.app.ProductSkuSimpleVO;
import com.msb.mall.product.model.vo.app.ShoppingCartVO;
import com.msb.mall.product.service.convert.ShoppingCartConvert;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;


/**
 * (ShoppingCart)表服务实现类
 *
 * @author makejava
 * @date 2022-03-31 16:16:10
 */
@Service("shoppingCartService")
public class ShoppingCartService extends ServiceImpl<ShoppingCartMapper, ShoppingCart> {

    @Resource
    private ShoppingCartConvert shoppingCartConvert;

    @Resource
    private ProductService productService;

    @Resource
    private ProductSkuService productSkuService;

    private AddShoppingCartVO checkIsMoreThanStock(Long productId, Long productSkuId, Integer shoppingCartNumber) {
        ProductSkuSimpleVO simpleSku = productSkuService.getSimpleSku(productSkuId);
        ProductSimpleVO simpleProduct = productService.getSimpleProduct(productId);
        Integer minBuyLimit;
        if (simpleProduct.getSingleBuyLimit().equals(0)) {
            minBuyLimit = simpleSku.getStock();
        } else {
            minBuyLimit = Math.min(simpleSku.getStock(), simpleProduct.getSingleBuyLimit());
        }
        AddShoppingCartVO addShoppingCartVO = new AddShoppingCartVO()
                .setProductSimple(simpleProduct)
                .setProductSkuSimple(simpleSku)
                .setShoppingCartNumber(shoppingCartNumber)
                .setIsBeyondMaxLimit(minBuyLimit < shoppingCartNumber);
        return addShoppingCartVO.setCanSetShoppingCartNumber(addShoppingCartVO.getIsBeyondMaxLimit() ? minBuyLimit : shoppingCartNumber);
    }

    /**
     * 查询当前用户的购物车信息
     *
     * @param userId 用户id
     * @return 购物车列表
     */
    public List<ShoppingCartVO> getCurrentUserShoppingCart(Long userId) {
        List<ShoppingCart> list = this.lambdaQuery().eq(ShoppingCart::getUserId, userId).orderByDesc(ShoppingCart::getCreateTime).list();
        List<ShoppingCartVO> shoppingCartListVO = shoppingCartConvert.toVo(list);
        shoppingCartListVO.forEach(shoppingCartVO -> {
            ProductSimpleVO productSimpleVO = Optional.ofNullable(productService.getSimpleProduct(shoppingCartVO.getProductId())).orElseGet(() -> new ProductSimpleVO()
                    .setId(shoppingCartVO.getId())
                    .setName(shoppingCartVO.getProductName())
                    .setMainPicture(shoppingCartVO.getProductMainPicture())
                    .setIsEnable(false));
            shoppingCartVO.setProduct(productSimpleVO);
            shoppingCartVO.setProductType(productSimpleVO.getProductType());
            shoppingCartVO.setProductSku(productSkuService.getSimpleSku(shoppingCartVO.getProductSkuId()));
        });
        return shoppingCartListVO;
    }

    /**
     * 库存设值
     *
     * @param userId 用户id
     * @param id     购物车id
     * @param number 设置数量
     * @return 产品及sku信息
     */
    public AddShoppingCartVO setShoppingCart(Long userId, Long id, Integer number) {
        ShoppingCart shoppingCart = this.getById(id);
        Optional.ofNullable(shoppingCart).orElseThrow(() -> new BizException("找不到购物车记录"));
        AddShoppingCartVO addShoppingCartVO = checkIsMoreThanStock(shoppingCart.getProductId(), shoppingCart.getProductSkuId(), number);
        this.lambdaUpdate().set(ShoppingCart::getNumber, addShoppingCartVO.getCanSetShoppingCartNumber()).eq(ShoppingCart::getId, id).update();
        return addShoppingCartVO;
    }


    /**
     * 库存加减
     *
     * @param userId          用户id
     * @param shoppingCartDTO 购物车dto
     * @return
     */
    public AddShoppingCartVO increaseShoppingCart(Long userId, AddShoppingCartDTO shoppingCartDTO) {
        Optional<ShoppingCart> shoppingCartOptional = this.lambdaQuery().eq(ShoppingCart::getUserId, UserContext.getUserId()).eq(ShoppingCart::getProductId, shoppingCartDTO.getProductId())
                .eq(ShoppingCart::getProductSkuId, shoppingCartDTO.getProductSkuId()).oneOpt();
        shoppingCartOptional.ifPresent(shoppingCart -> shoppingCart.setNumber(shoppingCart.getNumber() + shoppingCartDTO.getNumber()));
        ProductSimpleVO simpleProduct = productService.getSimpleProduct(shoppingCartDTO.getProductId());
        BizAssert.notNull(simpleProduct, "找不到商品");
        ShoppingCart shoppingCart = shoppingCartOptional.orElseGet(() -> ShoppingCart.builder()
                .userId(UserContext.getUserId())
                .number(shoppingCartDTO.getNumber())
                .productId(shoppingCartDTO.getProductId())
                .productSkuId(shoppingCartDTO.getProductSkuId())
                .productName(simpleProduct.getName())
                .productMainPicture(simpleProduct.getMainPicture()).build());
        AddShoppingCartVO addShoppingCartVO = checkIsMoreThanStock(shoppingCart.getProductId(), shoppingCart.getProductSkuId(), shoppingCart.getNumber());
        shoppingCart.setNumber(addShoppingCartVO.getCanSetShoppingCartNumber());
        this.saveOrUpdate(shoppingCart);
        return addShoppingCartVO;
    }

    public Boolean delete(Long userId, List<Long> idList) {
        return this.removeByIds(idList);
    }
}

