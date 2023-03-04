package com.msb.im.module.waiter.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.msb.im.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 店铺配置
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("store_config")
public class StoreConfig extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Integer sysId;
    private String name;
    private String avatar;

    @TableLogic
    private Boolean isDeleted;
}
