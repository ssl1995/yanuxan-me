package com.msb.mall.trade.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.msb.framework.mysql.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;


/**
 * 销售额统计表(SalesStatistics)表实体类
 *
 * @author shumengjiao
 * @since 2022-05-30 20:30:19
 */
@Data
@Accessors(chain = true)
@TableName("sales_statistics")
public class SalesStatistics implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;
  
    /**
     * 记录日期 
     */
    private LocalDate recordDate;
  
    /**
     * 销售额 
     */
    private BigDecimal sales;
  
}

