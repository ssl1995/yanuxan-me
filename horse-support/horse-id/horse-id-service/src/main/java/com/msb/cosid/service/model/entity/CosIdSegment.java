package com.msb.cosid.service.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.msb.framework.mysql.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 分布式IDP:号段模式的Dao数据结构
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("cosid_segment")
public class CosIdSegment extends BaseEntity {

    @TableId
    private Long id;
    /**
     * 业务标识
     */
    private String businessId;

    /**
     * 自增的最大值
     */
    private Long autoIncrement;
    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;
}
