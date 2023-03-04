package com.msb.framework.demo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品
 *
 * @author R
 */
@Data
public class Product {
    private Long id;
    private String name;
    private BigDecimal price;
}
