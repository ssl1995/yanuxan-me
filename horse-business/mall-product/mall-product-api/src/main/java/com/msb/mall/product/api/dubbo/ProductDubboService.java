package com.msb.mall.product.api.dubbo;

import com.msb.mall.product.api.model.*;

import java.util.List;

/**
 * 商品dubbo服务
 *
 * @author luozhan
 */
public interface ProductDubboService {

    /**
     * 根据skuId列表查询产品sku信息
     *
     * @param skuIdList skuId列表
     * @return list
     */
    List<ProductSkuDO> listProductSku(List<Long> skuIdList);

    /**
     * 根据skuId查询产品sku信息
     *
     * @param skuId skuId
     * @return sku信息
     */
    ProductSkuDO getProductSku(Long skuId);

    /**
     * 根据商品id查询对应sku信息
     *
     * @param productId 商品id
     * @return list
     */
    List<ProductSkuDO> listProductSku(Long productId);

    /**
     * 检查并扣减库存
     *
     * @param productSkuOccupyList skuId和数量
     */
    void checkAndReduceStock(List<ProductSkuOccupyDTO> productSkuOccupyList);

    /**
     * 批量返还商品库存
     *
     * @param returnStockDTOList：返还库存DTO列表
     * @return void
     * @author peng.xy
     * @date 2022/5/16
     */
    void batchReturnStock(List<ProductSkuReturnStockDTO> returnStockDTOList);

    /**
     * 返还商品库存
     *
     * @param returnStockDTO：返还库存DTO
     * @return void
     * @author peng.xy
     * @date 2022/5/16
     */
    void returnStock(ProductSkuReturnStockDTO returnStockDTO);

    /**
     * 根据产品id 获取产品信息
     *
     * @param id
     * @return
     */
    ProductDO getProductById(Long id);

    /**
     * 根据skuId查询虚拟发货信息
     * @param skuIdList skuId集合
     * @return 虚拟发货信息
     */
    List<ProductVirtualShipDO> listVirtualShip(List<Long> skuIdList);

    /**
     * 查询商品总览信息
     * @return ProductOverviewDO
     */
    ProductOverviewDO getProductOverview();

    /**
     * 查询商品信息
     * @param productDTO 商品DTO
     * @return 商品列表
     */
    List<ProductDO> listProduct(ProductDTO productDTO);

    /**
     * 根据产品id 获取产品信息
     *
     * @param idList
     * @return
     */
    List<ProductDO> listProductById(List<Long> idList);

}
