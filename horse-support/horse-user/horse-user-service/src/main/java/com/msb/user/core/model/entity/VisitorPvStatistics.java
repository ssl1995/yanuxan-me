package com.msb.user.core.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.msb.framework.mysql.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;



/**
 * 访客pv统计表(VisitorPvStatistics)表实体类
 *
 * @author shumengjiao
 * @since 2022-06-07 19:40:45
 */
@Data
@Accessors(chain = true)
@TableName("visitor_pv_statistics")
public class VisitorPvStatistics implements Serializable {

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

