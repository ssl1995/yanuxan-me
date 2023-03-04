package com.msb.mall.product.api.model;


import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 商品(Product)表实体类
 *
 * @author luozhan
 * @date 2022-03-29 14:37:39
 */
@Data
public class ProductDO implements Serializable {


    /**
     * 商品ID
     */
    private Long id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品分类ID
     */
    private Long categoryId;

    /**
     * 起售价
     */
    private BigDecimal startingPrice;


    /**
     * 商品主图
     */
    private String mainPicture;

    /**
     * 偏远地区邮费
     */
    private BigDecimal remoteAreaPostage;

    /**
     * 单次购买上限
     */
    private Integer singleBuyLimit;

    /**
     * 上下架状态:1-上架,0-下架
     */
    private Boolean isEnable;

    /**
     * 商品备注
     */
    private String remark;
}

