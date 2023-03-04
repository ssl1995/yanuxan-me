package com.msb.mall.product.model.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * 商品详情表(ProductDetail)表实体类
 *
 * @author luozhan
 * @date 2022-04-11 10:31:50
 */
@Data
@Accessors(chain = true)
@TableName("product_detail")
public class ProductDetail implements Serializable {

    /**
     * 商品id
     */
    @TableId
    private Long productId;

    /**
     * 商品详情描述
     */
    private String detail;

}
