package com.msb.mall.product.api.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 商品虚拟发货DO
 * @author shumengjiao
 * @date 2022-05-25
 */
@Data
public class VirtualProductDO implements Serializable {
    /**
     * 发货类型 1-文件 2-消息
     */
    private Integer shipType;
    /**
     * 发货内容
     */
    private String shipContent;
}
