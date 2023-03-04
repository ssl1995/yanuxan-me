package com.msb.user.core.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.msb.framework.mysql.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;



/**
 * 访客统计表(VisitorStatistics)表实体类
 *
 * @author shumengjiao
 * @since 2022-06-07 12:55:14
 */
@Data
@Accessors(chain = true)
@TableName("visitor_uv_statistics")
public class VisitorUvStatistics implements Serializable {

    /**
     * 访客统计id 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
  
    /**
     * 记录日期 
     */
    private LocalDate recordDate;
  
    /**
     * 访客数量 
     */
    private Integer count;
  
}

