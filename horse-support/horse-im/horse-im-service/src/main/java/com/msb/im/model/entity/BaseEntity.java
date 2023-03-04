package com.msb.im.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * @author liao
 */
@Getter
@Setter
public class BaseEntity implements Serializable {
    /**
     * 创建人（id）
     */
    @TableField(fill = FieldFill.INSERT)
    private String createUser;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 更新人（id）
     */
    @TableField(fill = FieldFill.UPDATE)
    private String updateUser;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
}
