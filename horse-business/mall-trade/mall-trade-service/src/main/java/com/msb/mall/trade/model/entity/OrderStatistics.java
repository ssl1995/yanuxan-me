package com.msb.mall.trade.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.msb.framework.mysql.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;



/**
 * 订单统计表(OrderStatistics)表实体类
 *
 * @author shumengjiao
 * @since 2022-05-30 20:17:31
 */
@Data
@Accessors(chain = true)
@TableName("order_statistics")
public class OrderStatistics implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;
  
    /**
     * 记录日期 
     */
    private LocalDate recordDate;
  
    /**
     * 数量 
     */
    private Integer count;
  
}

