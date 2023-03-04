package com.msb.mall.product.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.msb.framework.mysql.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;



/**
 * 虚拟商品表(VirtualProduct)表实体类
 *
 * @author shumengjiao
 * @since 2022-05-25 10:13:39
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("virtual_product")
public class VirtualProduct extends BaseEntity implements Serializable {

    /**
     * 虚拟商品id 
     */
    @TableId(type = IdType.AUTO)
    private Long id;
        
    /**
     * 发货类型（1-文件 2-消息） 
     */
    private Integer shipType;
        
    /**
     * 发货内容 
     */
    private String shipContent;

    /**
     * 商品id 
     */
     private Long productId;

        
}

