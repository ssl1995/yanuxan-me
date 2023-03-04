package com.msb.mall.product.dubbo;

import com.msb.framework.common.utils.ListUtil;
import com.msb.framework.web.result.BizAssert;
import com.msb.mall.product.api.dubbo.ProductDubboService;
import com.msb.mall.product.api.model.*;
import com.msb.mall.product.model.entity.Product;
import com.msb.mall.product.model.entity.ProductSku;
import com.msb.mall.product.model.vo.admin.VirtualProductVO;
import com.msb.mall.product.service.ProductService;
import com.msb.mall.product.service.ProductSkuService;
import com.msb.mall.product.service.VirtualProductService;
import com.msb.mall.product.service.convert.ProductConvert;
import com.msb.mall.product.service.convert.ProductSkuConvert;
import com.msb.mall.product.service.convert.VirtualProductConvert;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 产品DubboService
 *
 * @author luozhan
 */
@DubboService
@Slf4j
public class ProductDubboServiceImpl implements ProductDubboService {
    @Resource
    private ProductSkuService productSkuService;
    @Resource
    private ProductService productService;
    @Resource
    private VirtualProductService virtualProductService;

    @Resource
    private ProductSkuConvert productSkuConvert;
    @Resource
    private VirtualProductConvert virtualProductConvert;
    @Resource
    private ProductConvert productConvert;

    @Override
    public List<ProductSkuDO> listProductSku(List<Long> skuIdList) {
        List<ProductSku> productSkuList = productSkuService.list(skuIdList);
        log.info("listProductSku：{}，{}", skuIdList, productSkuList);
        List<ProductSkuDO> productSkuDoList = productSkuConvert.toDo(productSkuList);
        // 由sku列表查询出所有相关商品
        List<Long> productIdList = ListUtil.convertDistinct(productSkuList, ProductSku::getProductId);
        List<Product> productList = productService.listProduct(productIdList);
        // 匹配sku列表和商品列表，将对应的商品信息设值到sku中
        ListUtil.match(productSkuDoList, productList, ProductSkuDO::getProductId, Product::getId, (productSkuDO, product) -> {
            BizAssert.isTrue(product != null, "查询不到商品信息，该商品已经不存在");
            productSkuDO.setProductId(product.getId());
            productSkuDO.setProductName(product.getName());
            productSkuDO.setProductPicture(product.getMainPicture());
            productSkuDO.setRemoteAreaPostage(product.getRemoteAreaPostage());
            // sku对应的商品下架时，sku也是不可用的
            productSkuDO.setIsEnable(product.getIsEnable() && productSkuDO.getIsEnable());
            // 设置商品类型
            productSkuDO.setProductType(product.getProductType());
        });
        return productSkuDoList;
    }

    @Override
    public ProductSkuDO getProductSku(Long skuId) {
        return productSkuConvert.toDo(productSkuService.getById(skuId));
    }

    @Override
    public List<ProductSkuDO> listProductSku(Long productId) {
        List<ProductSku> productSkuList = productSkuService.list(productId);
        return productSkuConvert.toDo(productSkuList);
    }

    @Override
    public void checkAndReduceStock(List<ProductSkuOccupyDTO> productSkuOccupyList) {
        productSkuService.checkAndReduceStock(productSkuOccupyList);
    }

    /**
     * 批量返还商品库存
     *
     * @param returnStockDTOList：返还库存DTO列表
     * @return void
     * @author peng.xy
     * @date 2022/5/16
     */
    @Override
    public void batchReturnStock(List<ProductSkuReturnStockDTO> returnStockDTOList) {
        productSkuService.batchReturnStock(returnStockDTOList);
    }

    /**
     * 返还商品库存
     *
     * @param returnStockDTO：返还库存DTO
     * @return void
     * @author peng.xy
     * @date 2022/5/16
     */
    @Override
    public void returnStock(ProductSkuReturnStockDTO returnStockDTO) {
        productSkuService.returnStock(returnStockDTO);
    }

    @Override
    public ProductDO getProductById(Long id) {
        return productService.getProductDO(id);
    }

    /**
     * 根据skuId查询虚拟发货信息
     * @param skuIdList skuId集合
     * @return 虚拟发货信息
     */
    @Override
    public List<ProductVirtualShipDO> listVirtualShip(List<Long> skuIdList) {
        List<ProductVirtualShipDO> virtualShipList = new ArrayList<>();
        // 1、根据skuId查询出productId
        List<ProductSku> list = productSkuService.list(skuIdList);
        // 2、根据productId查询出虚拟发货信息
        for (ProductSku productSku : list) {
            List<VirtualProductVO> virtualProductVOS = virtualProductService.listVirtualProduct(productSku.getProductId());
            List<VirtualProductDO> virtualProductDOList = virtualProductConvert.toDo(virtualProductVOS);
            ProductVirtualShipDO productVirtualShipDO = ProductVirtualShipDO
                    .builder()
                    .skuId(productSku.getId())
                    .virtualProductDOList(virtualProductDOList)
                    .build();
            virtualShipList.add(productVirtualShipDO);
        }
        return virtualShipList;
    }

    /**
     * 查询商品总览信息
     * @return 商品总览信息
     */
    @Override
    public ProductOverviewDO getProductOverview() {
        // 已上架
        int onTheShelfCount = productService.lambdaQuery().eq(Product::getIsDeleted, Boolean.FALSE).eq(Product::getIsEnable, Boolean.TRUE).count();

        // 未上架
        int takeDownCount = productService.lambdaQuery().eq(Product::getIsDeleted, Boolean.FALSE).eq(Product::getIsEnable, Boolean.FALSE).count();

        // 库存紧张
        Integer stockWarnCount = productSkuService.getStockWarnCount();

        // 全部商品
        int allCount = productService.lambdaQuery().eq(Product::getIsDeleted, Boolean.FALSE).count();

        return new ProductOverviewDO()
                .setOnTheShelfCount(onTheShelfCount)
                .setTakeDownCount(takeDownCount)
                .setStockLessCount(stockWarnCount)
                .setAllCount(allCount);
    }

    /**
     * 查询商品信息
     * @param productDTO 商品DTO
     * @return 商品列表
     */
    @Override
    public List<ProductDO> listProduct(ProductDTO productDTO) {
        List<Product> list = productService.lambdaQuery()
                .like(Objects.nonNull(productDTO.getName()), Product::getName, productDTO.getName())
                .list();
        return productConvert.toDo(list);
    }

    @Override
    public List<ProductDO> listProductById(List<Long> idList) {
        return productService.listProductDO(idList);
    }


}
