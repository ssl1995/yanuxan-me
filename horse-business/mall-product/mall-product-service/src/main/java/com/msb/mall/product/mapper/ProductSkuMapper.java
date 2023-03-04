package com.msb.mall.product.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.msb.mall.product.model.entity.ProductSku;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

/**
 * 商品sku(ProductSku)表数据库访问层
 *
 * @author luozhan
 * @date 2022-03-29 14:37:40
 */
public interface ProductSkuMapper extends BaseMapper<ProductSku> {
    /**
     * 增加库存
     *
     * @param skuId   skuId
     * @param quality 数量（扣减库存用负数）
     * @return boolean
     */
    @Update("update product_sku set stock = stock + #{quality} where id = #{skuId}")
    boolean addStock(@Param("skuId") Long skuId, @Param("quality") Integer quality);

    /**
     * 扣减库存
     *
     * @param skuId   skuId
     * @param quality 数量
     * @return boolean
     */
    @Update("update product_sku set stock = stock - #{quality} where id = #{skuId} and stock >= #{quality}")
    boolean reduceStock(@Param("skuId") Long skuId, @Param("quality") Integer quality);

    /**
     * 统计商品总库存
     *
     * @param productId 商品id
     * @return 总库存
     */
    @Select("select sum(stock) from product_sku where product_id = #{productId}")
    BigDecimal getTotalStock(@Param("productId") Long productId);

    /**
     * 查询库存紧张的商品数量
     * @return
     */
    @Select("SELECT COUNT(*) FROM product WHERE id IN (SELECT DISTINCT product_id FROM product_sku WHERE stock_warn >= stock) AND is_deleted = 0 AND is_enable = 1")
    Integer getStockWarnCount();
}

