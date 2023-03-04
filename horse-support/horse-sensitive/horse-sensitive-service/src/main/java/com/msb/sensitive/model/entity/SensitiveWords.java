package com.msb.sensitive.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.msb.framework.mysql.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;



/**
 * 敏感词库(SensitiveWords)表实体类
 *
 * @author shumengjiao
 * @since 2022-06-15 15:14:10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sensitive_words")
public class SensitiveWords extends BaseEntity implements Serializable {

    /**
     * 敏感词ID 
     */
    @TableId(type = IdType.AUTO)
    private Long id;
  
    /**
     * 内容 
     */
    private String content;
  
    /**
     * 类型（1-反动词库,2-广告,3-政治类,4-敏感词,5-暴恐词,6-民生词库,7-涉枪涉爆违法信息关键词,8-色情词,9-other,10-广告高危词） 
     */
    private Integer wordsType;
  
 
 
 
 
    /**
     * 删除状态（0未删除，1已删除） 
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer isDeleted;
  
    /**
     * 使用状态（0禁用，1启用） 
     */
    private Integer enabled;
  
}

