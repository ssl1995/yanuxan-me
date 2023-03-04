package com.msb.mall.product.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.msb.framework.mysql.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 商品(Product)表实体类
 *
 * @author luozhan
 * @date 2022-03-30 18:04:03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("product")
public class Product extends BaseEntity implements Serializable {

    /**
     * 商品ID
     */
    @TableId(type = IdType.AUTO)
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

    /**
     * 逻辑删除字段
     */
    @TableLogic
    private Boolean isDeleted;

    /**
     * 商品类型 1-实物商品 2-虚拟商品
     */
    private Integer productType;
}
