package com.msb.mall.marketing.api;

/**
 * 商品推荐
 */
public interface ProductRecommendDubboService {

    /**
     * 设置商品为推荐商品
     *
     * @param productId 商品id
     * @return Boolean
     */
    Boolean setProductRecommend(Long productId);


    /**
     * 删除
     * @param productId
     * @return
     */
    Boolean delProductRecommend(Long productId);

    /**
     * 查询商品是否为推荐商品
     *
     * @param productId 商品id
     * @return Boolean
     */
    Boolean getProductRecommend(Long productId);
}
