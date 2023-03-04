package com.msb.mall.base.model.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.msb.framework.mysql.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * 数据字典表（任何有可能修改的以及有业务含义的数据字典或者前端需要用来展示下拉框）(Dictionary)表实体类
 *
 * @author makejava
 * @date 2022-03-31 15:04:13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("dictionary")
public class Dictionary extends BaseEntity implements Serializable {

    @TableId
    private Integer id;

    /**
     * 字典分类，使用大写驼峰命名
     */
    private String type;

    /**
     * 描述
     */
    @TableField("`desc`")
    private String desc;

    /**
     * 编码值，编码值应无任何业务意义，使用1，2，3...设值
     */
    @TableField("`code`")
    private Integer code;

    /**
     * 文本
     */
    private String text;

    /**
     * 状态（0-停用，1-开启）
     */
    private Boolean isEnable;

}
